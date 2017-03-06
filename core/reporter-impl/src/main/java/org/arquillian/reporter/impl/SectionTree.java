package org.arquillian.reporter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.Standalone;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.utils.Validate;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTree<SECTIONTYPE extends SectionEvent<SECTIONTYPE, PAYLOAD_TYPE, ? extends SectionEvent>, PAYLOAD_TYPE extends Report> {

    private Logger log = Logger.getLogger(getClass().getName());

    private Identifier<SECTIONTYPE> rootIdentifier;
    private List<SectionTree> subtrees = new ArrayList<>();
    private PAYLOAD_TYPE associatedReport;
    private Class<PAYLOAD_TYPE> reportTypeClass;

    public SectionTree(Identifier<SECTIONTYPE> rootIdentifier, PAYLOAD_TYPE associatedReport,
        Class<PAYLOAD_TYPE> reportTypeClass) {
        this.rootIdentifier = rootIdentifier;
        this.associatedReport = associatedReport;
        this.reportTypeClass = reportTypeClass;
    }

    public Identifier<SECTIONTYPE> getRootIdentifier() {
        return rootIdentifier;
    }

    public void setRootIdentifier(Identifier<SECTIONTYPE> rootIdentifier) {
        this.rootIdentifier = rootIdentifier;
    }

    public List<SectionTree> getSubtrees() {
        return subtrees;
    }

    public void setSubtrees(List<SectionTree> subtrees) {
        this.subtrees = subtrees;
    }

    public PAYLOAD_TYPE getAssociatedReport() {
        return associatedReport;
    }

    public void setAssociatedReport(PAYLOAD_TYPE associatedReport) {
        this.associatedReport = associatedReport;
    }

    public Class<PAYLOAD_TYPE> getReportTypeClass() {
        return reportTypeClass;
    }

    public void setReportTypeClass(Class<PAYLOAD_TYPE> reportTypeClass) {
        this.reportTypeClass = reportTypeClass;
    }

    public SectionTree<SECTIONTYPE, PAYLOAD_TYPE> getCloneWithoutSubtrees() {
        return new SectionTree<>(getRootIdentifier(), associatedReport, reportTypeClass);
    }

    public void mergeSectionTree(SectionTree<SECTIONTYPE, PAYLOAD_TYPE> treeToMerge) {
        // merge this tree
        associatedReport.merge(treeToMerge.getAssociatedReport());

        // if there is some subtree to merge try to find corresponding subtree in the current tree
        if (!treeToMerge.getSubtrees().isEmpty()) {

            SectionTree subtreeToMerge = treeToMerge.getSubtrees().get(0);
            Identifier rootIdentifierSubtreeToMerge = subtreeToMerge.getRootIdentifier();
            SectionTree matchedSubtree = null;

            // check if current tree has any subtree
            if (!getSubtrees().isEmpty()) {
                // if yes then find either tree with same identifier or the latest one of the same type of section
                matchedSubtree = findWitSameIdentifierOrTheLatest(rootIdentifierSubtreeToMerge);
            }

            // have I find any matching subtree?
            if (matchedSubtree == null) {
                Report associatedReport = subtreeToMerge.getAssociatedReport();

                // if not check if there is any associated report in the subtree to be merged
                if (associatedReport == null) {
                    // if not it means that there was expected that this tree should already exist, so the tree has to be created
                    createMissingSubtreeAndMergeIt(rootIdentifierSubtreeToMerge, subtreeToMerge);

                } else {
                    // if yes then the associated report is the one that has been reported, so please add it into the report associated with this tree
                    Report reportToAssociate =
                        getAssociatedReport().addNewReport(associatedReport, subtreeToMerge.getReportTypeClass());

                    // if the returned report that should be associated with a new tree node is not null, then check if
                    // the section id is not empty
                    String sectionToAddId = rootIdentifierSubtreeToMerge.getSectionId();
                    if (reportToAssociate != null && Validate.isNotEmpty(sectionToAddId)) {

                        // if the previous conditions are true,then if the section id is equal to the standalone identifier
                        // and the section-event implements Standalone interface, then don't add the section in the section tree
                        Class sectionToAddEventClass = rootIdentifierSubtreeToMerge.getSectionEventClass();
                        if (!Standalone.getStandaloneId().equals(sectionToAddId)
                            || !Standalone.class.isAssignableFrom(sectionToAddEventClass)) {

                            subtreeToMerge.setAssociatedReport(reportToAssociate);
                            getSubtrees().add(subtreeToMerge);
                        }
                    }
                }
            } else {
                //if there is matching subtree then merge it
                matchedSubtree.mergeSectionTree(subtreeToMerge);
            }
        }

    }

    private SectionTree findWitSameIdentifierOrTheLatest(Identifier rootIdentifierSubtreeToMerge) {
        // check if the identifier has complete information
        if (Validate.isNotEmpty(rootIdentifierSubtreeToMerge.getSectionId())) {
            // find subtree with the same identifier
            List<SectionTree> filtered = getSubtrees()
                .stream()
                .filter(tree -> tree.getRootIdentifier().equals(rootIdentifierSubtreeToMerge))
                .collect(Collectors.toList());
            if (filtered != null && !filtered.isEmpty()) {
                return filtered.get(0);
            }

        } else {
            // find the latest tree of the same section type
            Class sectionClassFromEventTree = rootIdentifierSubtreeToMerge.getSectionEventClass();
            Optional<SectionTree> lastTreeOfType = getSubtrees()
                .stream()
                .filter(tree -> tree.getRootIdentifier().getSectionEventClass() == sectionClassFromEventTree)
                .reduce((first, second) -> second);
            if (lastTreeOfType.isPresent()) {
                return lastTreeOfType.get();
            }
        }
        return null;
    }

    private void createMissingSubtreeAndMergeIt(Identifier rootIdentifierSubtreeToMerge, SectionTree subtreeToMerge) {
        // get class and id of the missing section
        String sectionId = rootIdentifierSubtreeToMerge.getSectionId();
        Class subTreeSectionClass = rootIdentifierSubtreeToMerge.getSectionEventClass();
        // and use non-parametric constructor to construct
        SectionEvent dummySection = (SectionEvent) SecurityActions.newInstance(subTreeSectionClass);
        // from created dummy section get class of payload and create an instance
        AbstractReport dummyReport =
            (AbstractReport) SecurityActions.newInstance(dummySection.getReportTypeClass());
        // set name of dummy report to the section id specified in identifier
        dummyReport.setName(new UnknownStringKey(sectionId));
        // set dummy report as an associated report
        subtreeToMerge.setAssociatedReport(dummyReport);
        // and also into the current report structure
        getAssociatedReport().addNewReport(dummyReport, dummySection.getReportTypeClass());
        // crated a clone of the missing tree without any subtree
        SectionTree dummyTree = subtreeToMerge.getCloneWithoutSubtrees();
        // add the dummy tree
        getSubtrees().add(dummyTree);

        log.info(String.format("There hasn't been found a parent node with identifier \"%s + %s\" in the section tree. "
                                   + "Reporter creates a new node with corresponding identifiers and also a report node in the report tree with name = %s",
                               subTreeSectionClass, sectionId, sectionId));
        // and merge the subtree with the dummy tree
        dummyTree.mergeSectionTree(subtreeToMerge);
    }

    @Override
    public String toString() {
        return "SectionTree{" +
            "rootIdentifier=" + rootIdentifier +
            ", subtrees=" + subtrees +
            ", associatedReport=" + associatedReport +
            '}';
    }
}
