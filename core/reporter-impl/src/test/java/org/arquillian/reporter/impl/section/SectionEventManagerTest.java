package org.arquillian.reporter.impl.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.Utils;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Arquillian.class)
public class SectionEventManagerTest {

    private static ExecutionReport executionReport = new ExecutionReport();

    @Test
    @InSequence(1)
    public void testAddingTestSuiteSectionsWithReports() {

        List<TestSuiteSection> listOfTestSuiteSections = new SectionGeneratorAndProcessor<TestSuiteSection>() {
            @Override TestSuiteSection createNewInstance(int index) throws Exception {
                return new TestSuiteSection(
                    Utils.prepareReport(TestSuiteReport.class, "test-suite-name" + index, index + 1, index + 10),
                    "test-suite-section" + index);
            }
        }.generateSetOfSections(10, executionReport);

        verify(executionReport.getSectionTree(), listOfTestSuiteSections);
    }

    @Test
    @InSequence(1)
//    this is probably crazy - need to be more sophisticated
    public void testAddingTestClassSectionsWithReports() {
        ExecutionReport executionReport = new ExecutionReport();
        List<TestClassSection> listOfTestSuiteSections = new ArrayList<>();

        IntStream.range(0, 10).forEach(suiteIndex -> {
            listOfTestSuiteSections.addAll(new SectionGeneratorAndProcessor<TestClassSection>() {
                @Override TestClassSection createNewInstance(int index) throws Exception {
                    return new TestClassSection(
                        Utils.prepareReport(TestClassReport.class, "test-class-name" + index, index + 1, index + 10),
                        getClass(),
                        "test-suite-section" + suiteIndex);
                }
            }.generateSetOfSections(10, executionReport));
        });

        verify(executionReport.getSectionTree(), listOfTestSuiteSections);
    }

    public void verify(SectionTree parentSectionTree, List<? extends SectionEvent> expectedSubsections) {
        // number of created subtrees should be same as the number of expected subsections
        assertThat(parentSectionTree.getSubtrees()).hasSize(expectedSubsections.size());

        expectedSubsections.stream().forEach(section -> {

            assertThat(section.isProcessed()).isTrue();

            List<SectionTree> subtrees = parentSectionTree.getSubtrees();
            Identifier<? extends SectionEvent> sectionIdentifier =
                new Identifier<>(section.getClass(), section.getSectionId());

            // filter subtrees to get the one with same identifier
            List<SectionTree> foundSubtreesWithSameIdentifier =
                subtrees.stream().filter(tree -> tree.getRootIdentifier().equals(sectionIdentifier))
                    .collect(Collectors.toList());
            Assertions.assertThat(foundSubtreesWithSameIdentifier).hasSize(1);

            // get the matched subtree
            SectionTree matchedSubtree = foundSubtreesWithSameIdentifier.get(0);
            // and check associated report
            assertThat(matchedSubtree.getAssociatedReport()).isEqualTo(section.getReport());

        });
    }

    abstract class SectionGeneratorAndProcessor<T extends SectionEvent> {

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
