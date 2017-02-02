package org.arquillian.reporter.impl.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;

import static org.arquillian.reporter.impl.asserts.SectionAssert.assertThatSection;
import static org.arquillian.reporter.impl.utils.Utils.prepareReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTreeEventManagerUtils {

    public static final int EXPECTED_NUMBER_OF_SECTIONS = 4;

    public static final int PARENT_COUNT_OF_COMPLEX_PREPARED_TREE =
        (int) (1  // test execution tree
            + (EXPECTED_NUMBER_OF_SECTIONS * 2) // test suite and test class trees
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2)); // test method trees

    public static final int TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE =
        (int) (PARENT_COUNT_OF_COMPLEX_PREPARED_TREE // number of parents
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2) * 2 // number of configs in test class and suite
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3) * 2); // number of configs and failures in method

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestSuiteSections(
        ExecutionReport executionReport) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();
        List<TestSuiteSection> testSuiteSections = new SectionGeneratorAndProcessor<TestSuiteSection>() {
            @Override TestSuiteSection createNewInstance(int index) throws Exception {
                return new TestSuiteSection(
                    prepareReport(TestSuiteReport.class,
                                  getTestSuiteReportName(index),
                                  index + 1,
                                  index + 10),
                    getTestSuiteSectionName(index));
            }
        }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport);

        sections.put(executionReport.getExecutionSection(), testSuiteSections);
        return sections;
    }

    public static String getTestSuiteReportName(int index) {
        return "test-suite-report-" + index;
    }

    public static String getTestSuiteSectionName(int index) {
        return "test-suite-section-" + index;
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestSuiteConfigurationSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestSuiteSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestSuiteSections.stream().forEach(suite -> {
            List<TestSuiteConfigurationSection> testSuiteConfigSections =
                new SectionGeneratorAndProcessor<TestSuiteConfigurationSection>() {
                    @Override
                    TestSuiteConfigurationSection createNewInstance(int index) throws Exception {
                        String suiteId = suite.getSectionId();
                        return new TestSuiteConfigurationSection(
                            prepareReport(ConfigurationReport.class,
                                          getConfigReportName(index, getTestSuiteNameSuffix(suiteId)),
                                          index + 1,
                                          index + 10),
                            suiteId,
                            getTestSuiteConfigSectionName(index));
                    }
                }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport);
            sections.put((TestSuiteSection) suite, testSuiteConfigSections);
        });

        return sections;
    }

    public static String getConfigReportName(int index, String suffix) {
        return String.format("configuration-report-%s-%s", index, suffix);
    }

    public static String getTestSuiteNameSuffix(String testSuiteId) {
        return String.format("in-suite-%s", testSuiteId);
    }

    public static String getTestSuiteConfigSectionName(int index) {
        return "test-suite-configuration-section-" + index;
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestClassSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestSuiteSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestSuiteSections.stream().forEach(suite -> {
            List<TestClassSection> testClassSections = new SectionGeneratorAndProcessor<TestClassSection>() {
                @Override
                TestClassSection createNewInstance(int index) throws Exception {
                    String suiteId = suite.getSectionId();

                    return new TestClassSection(
                        prepareReport(
                            TestClassReport.class,
                            getTestClassReportName(index, suiteId),
                            index + 1,
                            index + 10),
                        DummyTestClass.class,
                        suiteId);
                }
            }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport);
            // add only one test-class section as it is processed using same class, all of them should be merged info one report
            sections.put((TestSuiteSection) suite, testClassSections.subList(0, 1));
        });

        return sections;
    }

    public static String getTestClassReportName(int index, String suiteId) {
        return String.format("test-class-report-name-%s-in-suite-%s", index, suiteId);
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestClassConfigurationSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestClassSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestClassSections.stream().forEach(tc -> {
            String testSuiteId = ((TestClassSection) tc).getTestSuiteId();
            sections
                .put((TestClassSection) tc, new SectionGeneratorAndProcessor<TestClassConfigurationSection>() {
                    @Override
                    TestClassConfigurationSection createNewInstance(int index) throws Exception {
                        TestClassConfigurationSection testClassConfigurationSection = new TestClassConfigurationSection(
                            prepareReport(
                                ConfigurationReport.class,
                                getConfigReportName(index, getTestClassNameSuffix(tc.getSectionId(), testSuiteId)),
                                index + 1,
                                index + 10),
                            DummyTestClass.class,
                            getTestClassConfigSectionName(index));
                        testClassConfigurationSection.setTestSuiteId(testSuiteId);
                        return testClassConfigurationSection;
                    }
                }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport));
        });

        return sections;
    }

    public static String getTestClassNameSuffix(String classId, String suiteId) {
        return String.format("-in-test-class-%s-in-suite-%s", classId, suiteId);
    }

    public static String getTestClassConfigSectionName(int index) {
        return "test-class-configuration-section-" + index;
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestMethodSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestClassSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestClassSections.stream().forEach(tc -> {
            String testSuiteId = ((TestClassSection) tc).getTestSuiteId();
            sections.put((TestClassSection) tc, new SectionGeneratorAndProcessor<TestMethodSection>() {
                @Override
                TestMethodSection createNewInstance(int index) throws Exception {
                    TestMethodSection testMethodSection = new TestMethodSection(
                        prepareReport(
                            TestMethodReport.class,
                            getTestMethodReportName(index, testSuiteId, tc.getSectionId()),
                            index + 1,
                            index + 10),
                        getTestMethodSectionName(index));
                    testMethodSection.setTestSuiteId(testSuiteId);
                    return testMethodSection;
                }
            }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport));
        });

        return sections;
    }

    public static String getTestMethodReportName(int index, String suiteId, String classId) {
        return String.format("test-method-report-name-%s-in-test-class-%s-in-suite-%s",
                             index, classId, suiteId);
    }

    public static Method getTestMethodSectionName(int index) {
        return DummyTestClass.class.getDeclaredMethods()[index];
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestMethodConfigurationSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestMethodSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestMethodSections.stream().forEach(tm -> {
            TestMethodSection testMethodSection = (TestMethodSection) tm;
            String testSuiteId = testMethodSection.getTestSuiteId();
            sections.put(testMethodSection, new SectionGeneratorAndProcessor<TestMethodConfigurationSection>() {
                @Override
                TestMethodConfigurationSection createNewInstance(int index) throws Exception {
                    TestMethodConfigurationSection testMethodConfigSection =
                        new TestMethodConfigurationSection(
                            prepareReport(
                                ConfigurationReport.class,
                                getConfigReportName(index, getTestMethodNameSuffix(tm.getSectionId(), testSuiteId)),
                                index + 1,
                                index + 10),
                            testMethodSection.getMethod(),
                            getTestMethodConfigSectionName(index));
                    testMethodConfigSection.setTestSuiteId(testSuiteId);
                    return testMethodConfigSection;
                }
            }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport));
        });

        return sections;
    }

    public static String getTestMethodNameSuffix(String methodId, String suiteId) {
        return String.format("in-test-method-%s-in-suite-%s", methodId, suiteId);
    }

    public static String getTestMethodConfigSectionName(int index) {
        return "test-method-configuration-report-" + index;
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> feedWithTestMethodFailureSections(
        ExecutionReport executionReport, List<SectionEvent> parentTestMethodSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestMethodSections.stream().forEach(tm -> {
            TestMethodSection testMethodSection = (TestMethodSection) tm;
            String testSuiteId = testMethodSection.getTestSuiteId();
            sections.put(testMethodSection, new SectionGeneratorAndProcessor<TestMethodFailureSection>() {
                @Override
                TestMethodFailureSection createNewInstance(int index) throws Exception {
                    TestMethodFailureSection testMethodFailureSection =
                        new TestMethodFailureSection(
                            prepareReport(
                                FailureReport.class,
                                getFailureReportName(index, getTestMethodNameSuffix(tm.getSectionId(), testSuiteId)),
                                index + 1,
                                index + 10),
                            testMethodSection.getMethod(),
                            getTestMethodFailureSectionName(index));
                    testMethodFailureSection.setTestSuiteId(testSuiteId);
                    return testMethodFailureSection;
                }
            }.generateSetOfSections(EXPECTED_NUMBER_OF_SECTIONS, executionReport));
        });

        return sections;
    }

    public static String getFailureReportName(int index, String suffix) {
        return String.format("failure-report-%s-%s", index, suffix);
    }

    public static String getTestMethodFailureSectionName(int index) {
        return "test-method-failure-report-" + index;
    }

    public static List<SectionEvent> getSubsectionsOfSomeSection(Class<? extends SectionEvent> parentClass,
        Map<SectionEvent, List<? extends SectionEvent>> sections) {
        return sections.keySet().stream().map(parent -> {
            if (parent.getClass().equals(parentClass)) {
                return sections.get(parent);
            }
            return new ArrayList<SectionEvent>();
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    public static <T extends SectionEvent> List<T> getParentSectionsOfSomeType(Class<? extends T> sectionClass,
        Map<SectionEvent, List<? extends SectionEvent>> sections) {
        return sections.keySet().stream().map(parent -> {
            if (parent.getClass().equals(sectionClass)) {
                return (T) parent;
            }
            return null;
        }).filter(section -> section != null).collect(Collectors.toList());
    }

    public static void verifyAllSectionsAreProcessed(Map<SectionEvent, List<? extends SectionEvent>> sections) {
        sections.keySet().stream().forEach(parent -> {
            sections.get(parent).stream().forEach(child -> assertThatSection(child).isProccessed());
        });
    }

    public static Map<SectionEvent, List<? extends SectionEvent>> prepareSectionTreeWithReporterCoreSectionsAndReports(
        ExecutionReport executionReport) {
        // add test suite sections
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        List<SectionEvent> testSuiteSubsections = getSubsectionsOfSomeSection(ExecutionSection.class, sections);

        // add test class sections
        sections.putAll(feedWithTestClassSections(executionReport, testSuiteSubsections));
        List<SectionEvent> testClassSubsections = getSubsectionsOfSomeSection(TestSuiteSection.class, sections);

        // add test method sections
        sections.putAll(feedWithTestMethodSections(executionReport, testClassSubsections));
        List<SectionEvent> testMethodSubsections = getSubsectionsOfSomeSection(TestClassSection.class, sections);

        // add test class config sections
        mergeMapOfSections(sections, feedWithTestClassConfigurationSections(executionReport, testClassSubsections));

        // add test suite config sections
        mergeMapOfSections(sections, feedWithTestSuiteConfigurationSections(executionReport, testSuiteSubsections));

        // add test method failure sections
        mergeMapOfSections(sections, feedWithTestMethodFailureSections(executionReport, testMethodSubsections));
        // add test method config sections
        mergeMapOfSections(sections, feedWithTestMethodConfigurationSections(executionReport, testMethodSubsections));

        return sections;
    }

    public static void mergeMapOfSections(Map<SectionEvent, List<? extends SectionEvent>> sections,
        Map<SectionEvent, List<? extends SectionEvent>> sectionsToMerge) {

        sectionsToMerge.keySet().forEach(key -> {
            if (sections.containsKey(key)) {
                ((List<SectionEvent>) sections.get(key)).addAll(sectionsToMerge.get(key));
            } else {
                sections.put(key, sectionsToMerge.get(key));
            }
        });

    }

    public static Optional<SectionTree> getTreeWithIdAndReportNameFromWholeTree(SectionTree tree,
        Identifier identifier, String name) {
        List<SectionTree> listOfTrees = new ArrayList<>();
        collectAllTrees(tree, listOfTrees);
        return getTreeWithIdAndReportNameFromList(listOfTrees, identifier, new UnknownStringKey(name));
    }

    public static Optional<SectionTree> getTreeWithIdAndReportNameFromList(List<SectionTree> list,
        Identifier identifier, StringKey name) {
        return list.stream()
            .filter(tree -> tree.getRootIdentifier().equals(identifier)
                && tree.getAssociatedReport().getName().equals(name))
            .findFirst();
    }

    public static Optional<SectionTree> getTreeWithIdentifierFromList(List<SectionTree> list, Identifier identifier) {
        return list.stream()
            .filter(tree -> tree.getRootIdentifier().equals(identifier))
            .findFirst();
    }

    public static Identifier getSectionIdentifier(SectionEvent section) {
        return new Identifier<>(section.getClass(), section.getSectionId());
    }

    public static void collectAllTrees(SectionTree tree, List<SectionTree> treeList) {
        List<SectionTree> subtrees = tree.getSubtrees();
        treeList.add(tree);
        if (!tree.getSubtrees().isEmpty()) {
            subtrees.stream().forEach(subtree -> collectAllTrees(subtree, treeList));
        }
    }

    abstract static class SectionGeneratorAndProcessor<T extends SectionEvent> {

        List<T> generateSetOfSections(int number, ExecutionReport executionReport) {
            return IntStream
                .range(0, number)
                .mapToObj(index -> {
                              try {
                                  T sectionInstance = createNewInstance(index);
                                  SectionEventManager.processEvent(sectionInstance, executionReport);
                                  return sectionInstance;
                              } catch (Exception e) {
                                  throw new RuntimeException(e);
                              }
                          }
                ).collect(Collectors.toList());
        }

        abstract T createNewInstance(int index) throws Exception;
    }

}
