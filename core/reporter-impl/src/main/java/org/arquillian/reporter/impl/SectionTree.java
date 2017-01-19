package org.arquillian.reporter.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public void merge(SectionTree<SECTIONTYPE, PAYLOAD_TYPE> treeToMerge) {
        associatedReport.merge(treeToMerge.getAssociatedReport());

        if (!treeToMerge.getSubtrees().isEmpty()) {

            SectionTree subtreeToMerge = treeToMerge.getSubtrees().get(0);
            Identifier rootIdentifierOfEventTree = subtreeToMerge.getRootIdentifier();
            SectionTree matchedTree = null;

            if (!getSubtrees().isEmpty()) {
                if (Validate.isNotEmpty(rootIdentifierOfEventTree.getSectionId())) {
                    List<SectionTree> filtered = getSubtrees()
                        .stream()
                        .filter(tree -> tree.getRootIdentifier().equals(rootIdentifierOfEventTree))
                        .collect(Collectors.toList());
                    if (filtered != null && !filtered.isEmpty()) {
                        matchedTree = filtered.get(0);
                    }
                } else {
                    Class sectionClassFromEventTree = rootIdentifierOfEventTree.getSectionEventClass();
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
                if (subtreeToMerge.getAssociatedReport() != null) {
                    getAssociatedReport().addNewReport(subtreeToMerge.getAssociatedReport());
                    getSubtrees().add(subtreeToMerge);

//                    Class<? extends AbstractReport> payloadClassToMerge =
//                        subtreeToMerge.getAssociatedReport().getClass();
//
//                    Method[] methods = getAssociatedReport().getClass().getMethods();
//
//                    List<Method> methodsToGetList =
//                        Arrays.stream(methods)
//                            .filter(
//                                method -> checkReturnListType(method, payloadClassToMerge))
//                            .collect(Collectors.toList());
//                    if (methodsToGetList != null && !methodsToGetList.isEmpty()) {
//                        getSectionList(getAssociatedReport(), methodsToGetList.get(0))
//                            .add(subtreeToMerge.getAssociatedReport());
//                    } else {
//                        getAssociatedReport().getSubreports().add(subtreeToMerge.getAssociatedReport());
//                    }

                    //                matchedTree = new SectionTree(rootIdentifierOfEventTree, subtreeOfEventTree.getAssociatedReport());
                    //                sectionTree.getSubtrees().add(matchedTree);
                } else {
                    // todo
                }
            } else {
                matchedTree.merge(subtreeToMerge);
            }
        }

    }

    private List<AbstractReport> getSectionList(AbstractReport parentSectionReport, Method method) {
        try {
            return (List<AbstractReport>) method.invoke(parentSectionReport);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

    private static boolean checkReturnListType(Method method, Class<?> expectedClass) {
        if (method.getReturnType().isAssignableFrom(List.class)) {

            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType != null) {

                if (genericReturnType instanceof ParameterizedType) {

                    Type[] parameters = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                    if (parameters.length == 1) {
                        return parameters[0].getTypeName().equals(expectedClass.getTypeName());
                    }
                }
            }
        }
        return false;
    }
}
