package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.builder.Validate;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ExecutionReport> report;

    private ExecutionReport report() {
        return report.get();
    }

    public void observeFirstEvent(@Observes ManagerStarted event) {
        if (report() == null) {
            ExecutionReport executionReport = new ExecutionReport();
            report.set(executionReport);
        }
    }

    public void observeSectionReports(@Observes(precedence = -100) SectionEvent event) {
        processEvent(event);
    }

    public void observeTestSuiteReports(@Observes TestSuiteSection event) {
        processEvent(event);
    }

    public void observeTestClassReports(@Observes TestClassSection event) {
//        AbstractReport lastTestSuiteReport = getLastTestSuiteReport();
//        if (lastTestSuiteReport != null) {
            processEvent(event);
//        }
    }

    public void observeTestMethodReports(@Observes TestMethodSection event) {
        processEvent(event);

    }

    public void observeReportTestSuiteConfigurationEvents(@Observes TestSuiteConfigurationSection event) {
//        TestSuiteReport lastTestSuiteReport = getLastTestSuiteReport();
//        if (lastTestSuiteReport != null) {
            processEvent(event);
//        }
    }

    private <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent>
    void processEvent(SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE> event) {

        if (event.isProcessed()){
            return;
        }

        //        if (parentEventClass == null || !event.getClass().isAssignableFrom(parentEventClass)) {
        //            processSubclassEvent(parentEventClass, event, parentSectionReport, methodNameToGetList);
        //        } else {



        SectionTree eventTree = createTreeRecursively(event, null);
        System.err.println("before:");
//        printJson();
        System.err.println("============");
        report().getSectionTree().merge(eventTree);
        System.err.println("after:");
//        printJson();
        System.err.println("============");
//        SectionTree treeToMerge = getSubtreeToMerge(report().getSectionTree(), eventTree);
//        if (treeToMerge.getAssociatedReport() != null) {
//            treeToMerge.getAssociatedReport().merge(event.getReport());
//        }

        //        List<Identifier> parentIdentifiers = new ArrayList<>();
        //        Identifier firstNotValid = buildListOfParentValidIdentifiers(event, parentIdentifiers);
        //        Identifier toppestValidIdentifier;
        //        getLastTreeOfType(report().getSectionTree().getLastSubtree(), firstNotValid.getSectionEventClass());

        //        AbstractReport alreadyExisting = report().getSectionReportByIdentifier(event);
        //            if (alreadyExisting != null) {
        //                alreadyExisting.merge(event.getReport());
        //            } else if (parentSectionReport != null) {
        //                getSectionList(parentSectionReport, methodNameToGetList).add(event.getReport());
        //                //                fire().getTestSuiteReports().getTestClassReports().add(event.getReport());
        //            } else if (event.getParentEvent() != null) {
        //                processParentEventIsSet(event, methodNameToGetList);
        //            } else {
        //                // todo
        //            }

        //        }

//        report().register(event);
        event.setProcessed(true);
    }

    private <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent>, REPORT_TYPE extends AbstractReport> SectionTree getSubtreeToMerge(
        SectionTree<SECTIONTYPE, REPORT_TYPE> sectionTree, SectionTree<SECTIONTYPE, REPORT_TYPE> eventTree) {
        SectionTree matchedTree = null;
        if (eventTree.getSubtrees().isEmpty()) {
            return sectionTree;
        }
        SectionTree subtreeOfEventTree = eventTree.getSubtrees().get(0);
        Identifier rootIdentifierOfEventTree = subtreeOfEventTree.getRootIdentifier();

        if (!sectionTree.getSubtrees().isEmpty()) {
            if (Validate.isNotEmpty(rootIdentifierOfEventTree.getSectionId())) {
                List<SectionTree> filtered = sectionTree
                    .getSubtrees()
                    .stream()
                    .filter(tree -> tree.getRootIdentifier().equals(rootIdentifierOfEventTree))
                    .collect(Collectors.toList());
                if (filtered != null && !filtered.isEmpty()) {
                    matchedTree = filtered.get(0);
                }
            } else {
                Class sectionClassFromEventTree = rootIdentifierOfEventTree.getSectionEventClass();
                Optional<SectionTree> lastTreeOfType = sectionTree
                    .getSubtrees()
                    .stream()
                    .filter(tree -> tree.getRootIdentifier().getSectionEventClass() == sectionClassFromEventTree)
                    .reduce((first, second) -> second);
                if (lastTreeOfType.isPresent()) {
                    matchedTree = lastTreeOfType.get();
                } else {
                    // todo
                }
            }
        }

        if (matchedTree == null) {
            matchedTree = new SectionTree(rootIdentifierOfEventTree, subtreeOfEventTree.getAssociatedReport());
            sectionTree.getSubtrees().add(matchedTree);

        }

        return getSubtreeToMerge(matchedTree, subtreeOfEventTree);
    }

    private <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent>, REPORT_TYPE extends AbstractReport>
    SectionTree<SECTIONTYPE, REPORT_TYPE> createTreeRecursively(
        SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent> sectionEvent,
        SectionTree<SECTIONTYPE, REPORT_TYPE> subtree) {

        ExecutionReport report = report();

        if (sectionEvent == null){
            return subtree;
        }

        SectionTree<SECTIONTYPE, REPORT_TYPE> sectionTree = new SectionTree<>(sectionEvent.identifyYourself(), sectionEvent.getReport());
        if (subtree != null) {
            sectionTree.getSubtrees().add(subtree);
        }

        SectionEvent parentSectionThisSectionBelongsTo = sectionEvent.getParentSectionThisSectionBelongsTo();
        if (parentSectionThisSectionBelongsTo == null && sectionEvent.getClass() == TestSuiteSection.class){
            parentSectionThisSectionBelongsTo = new ExecutionSection();
        }

        return createTreeRecursively(parentSectionThisSectionBelongsTo, sectionTree);
    }

    //    private Identifier buildListOfParentValidIdentifiers(SectionEvent sectionEvent,
    //        List<Identifier> parentIdentifiers) {
    //        SectionEvent parentSection = sectionEvent.getParentSectionThisSectionBelongsTo();
    //        Identifier identifier = parentSection.identifyYourself();
    //        if (Validate.isNotEmpty(identifier.getSectionId())) {
    //            parentIdentifiers.add(identifier);
    //            return buildListOfParentValidIdentifiers(parentSection, parentIdentifiers);
    //        } else {
    //            return identifier;
    //        }
    //    }

    //    private void processSubclassEvent(Class<? extends SectionEvent> expectedReportEventClass, SectionEvent actualEvent,
    //        AbstractReport parentSectionReport, String methodNameToGetList) {
    //
    //        AbstractReport alreadyExisting = report().getSectionReportByIdentifier(actualEvent);
    //        if (alreadyExisting != null) {
    //            alreadyExisting.merge(actualEvent.getReport());
    //            return;
    //        }
    //
    //        if (actualEvent.getParentEvent() != null) {
    //            processParentEventIsSet(actualEvent, null);
    //        } else {
    //            Class<?> superclass = actualEvent.getClass().getSuperclass();
    //            if (!superclass.isAssignableFrom(expectedReportEventClass)) {
    //                getSectionList(parentSectionReport, methodNameToGetList).add(actualEvent.getReport());
    //            }
    //        }
    //    }
    //
    //    private void processParentEventIsSet(SectionEvent actualEvent, String methodNameToGetList) {
    //
    //        SectionEvent parentEvent = actualEvent.getParentEvent();
    //        Identifier parentIdentifier = new Identifier(parentEvent.getClass(), parentEvent.getSectionId());
    //
    //        AbstractReport parentSection = report().getSectionReportByIdentifier(parentIdentifier);
    //        if (parentSection != null) {
    //            if (methodNameToGetList != null) {
    //                getSectionList(parentSection, methodNameToGetList).add(actualEvent.getReport());
    //            } else {
    //                parentSection.getSubreports().add(actualEvent.getReport());
    //            }
    //        } else {
    //            // todo
    //            return;
    //        }
    //    }



    public void afterSuite(@Observes ManagerStopping event) throws IOException {
        printJson();
    }

    private void printJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report().getTestSuiteReports());
        try {
            FileUtils.writeStringToFile(new File("target/fire.json"), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // todo support multiple test suites within one execution
    private TestSuiteReport getLastTestSuiteReport() {
        List<TestSuiteReport> testSuiteReports = report().getTestSuiteReports();
        if (testSuiteReports.size() > 0) {
            return testSuiteReports.get(testSuiteReports.size() - 1);
        }
        return null;
    }

}
