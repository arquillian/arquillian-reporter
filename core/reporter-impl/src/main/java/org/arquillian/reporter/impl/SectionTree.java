package org.arquillian.reporter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.builder.Validate;
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
        associatedReport.merge(treeToMerge.getAssociatedReport());

        if (!treeToMerge.getSubtrees().isEmpty()) {

            SectionTree subtreeToMerge = treeToMerge.getSubtrees().get(0);
            Identifier rootIdentifierSubtreeToMerge = subtreeToMerge.getRootIdentifier();
            SectionTree matchedTree = null;

            if (!getSubtrees().isEmpty()) {
                if (Validate.isNotEmpty(rootIdentifierSubtreeToMerge.getSectionId())) {
                    List<SectionTree> filtered = getSubtrees()
                        .stream()
                        .filter(tree -> tree.getRootIdentifier().equals(rootIdentifierSubtreeToMerge))
                        .collect(Collectors.toList());
                    if (filtered != null && !filtered.isEmpty()) {
                        matchedTree = filtered.get(0);
                    }
                } else {
                    Class sectionClassFromEventTree = rootIdentifierSubtreeToMerge.getSectionEventClass();
                    Optional<SectionTree> lastTreeOfType = getSubtrees()
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
                AbstractReport reportToAssociate;
                if (subtreeToMerge.getAssociatedReport() == null) {
                    try {
                        Class subTreeSectionClass = rootIdentifierSubtreeToMerge.getSectionEventClass();
                        Constructor[] constructors = subTreeSectionClass.getConstructors();
                        Optional<Constructor> oneParamConstructor =
                            Arrays.stream(constructors).filter(c -> c.getParameters().length == 1).findFirst();
                        SectionEvent section;
                        if (oneParamConstructor.isPresent()) {
                             section = (SectionEvent) oneParamConstructor.get().newInstance(null);
                        } else {
                            section = (SectionEvent) subTreeSectionClass.newInstance();
                        }
                        AbstractReport dummyReport = (AbstractReport) section.getReportTypeClass().newInstance();
                        dummyReport.setName(rootIdentifierSubtreeToMerge.getSectionId());
                        subtreeToMerge.setAssociatedReport(dummyReport);
                        SectionTree dummyTree = subtreeToMerge.getCloneWithoutSubtrees();
                        reportToAssociate = getAssociatedReport().addNewReport(dummyReport);
                        getSubtrees().add(dummyTree);
                        dummyTree.merge(subtreeToMerge);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    // todo log
                }else {
                    reportToAssociate = getAssociatedReport().addNewReport(subtreeToMerge.getAssociatedReport());
                    subtreeToMerge.setAssociatedReport(reportToAssociate);
                    getSubtrees().add(subtreeToMerge);
                }
            } else {
                matchedTree.merge(subtreeToMerge);
            }
        }

    }
}
