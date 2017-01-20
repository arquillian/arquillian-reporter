package org.arquillian.reporter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.builder.Validate;
import org.arquillian.reporter.api.builder.impl.UnknownStringKey;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTree<SECTIONTYPE extends SectionEvent<SECTIONTYPE, PAYLOAD_TYPE, ? extends SectionEvent>, PAYLOAD_TYPE extends AbstractReport> {

    private Identifier<SECTIONTYPE> rootIdentifier;
    private List<SectionTree> subtrees = new ArrayList<>();
    private PAYLOAD_TYPE associatedReport;

    public SectionTree(Identifier<SECTIONTYPE> rootIdentifier, PAYLOAD_TYPE associatedReport) {
        this.rootIdentifier = rootIdentifier;
        this.associatedReport = associatedReport;
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

    public SectionTree<SECTIONTYPE, PAYLOAD_TYPE> getCloneWithoutSubtrees(){
        return new SectionTree<>(getRootIdentifier(), associatedReport);
    }

    public void merge(SectionTree<SECTIONTYPE, PAYLOAD_TYPE> treeToMerge) {
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

                // if not check if there is any associated report in the subtree to be merged
                if (subtreeToMerge.getAssociatedReport() == null) {
                    // if not it means that there was expected that this tree should already exist, so the tree has to be created
                    createMissingSubtreeAndMergeIt(rootIdentifierSubtreeToMerge, subtreeToMerge);

                }else {
                    // if yes then the associated report is the one that has been reported, so please add it into the report associated with this tree
                    AbstractReport reportToAssociate =
                        getAssociatedReport().addNewReport(subtreeToMerge.getAssociatedReport());
                    subtreeToMerge.setAssociatedReport(reportToAssociate);
                    getSubtrees().add(subtreeToMerge);
                }
            } else {
                //if there is matching subtree then merge it
                matchedSubtree.merge(subtreeToMerge);
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
        try {
            // get class of the missing section
            Class subTreeSectionClass = rootIdentifierSubtreeToMerge.getSectionEventClass();
            // and use non-parametric constructor to construct
            SectionEvent dummySection = (SectionEvent) subTreeSectionClass.newInstance();
            // from created dummy section get class of payload and create an instance
            AbstractReport dummyReport = (AbstractReport) dummySection.getReportTypeClass().newInstance();
            // set name of dummy report to the section id specified in identifier
            dummyReport.setName(new UnknownStringKey(rootIdentifierSubtreeToMerge.getSectionId()));
            // set dummy report as an associated report
            subtreeToMerge.setAssociatedReport(dummyReport);
            // and also into the current report structure
            getAssociatedReport().addNewReport(dummyReport);
            // crated a clone of the missing tree without any subtree
            SectionTree dummyTree = subtreeToMerge.getCloneWithoutSubtrees();
            // add the dummy tree
            getSubtrees().add(dummyTree);
            // and merge the subtree with the dummy tree
            dummyTree.merge(subtreeToMerge);

        } catch (InstantiationException e) {
            e.printStackTrace();
            // todo
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            // todo
        }
        // todo log
    }
}
