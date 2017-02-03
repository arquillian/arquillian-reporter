package org.arquillian.reporter.impl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.impl.SectionTree;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTreeEventManagerUtils {

    public static <T extends SectionEvent> List<T> getParentSectionsOfSomeType(Class<? extends T> sectionClass,
        Map<SectionEvent, List<? extends SectionEvent>> sections) {
        return sections.keySet().stream().map(parent -> {
            if (parent.getClass().equals(sectionClass)) {
                return (T) parent;
            }
            return null;
        }).filter(section -> section != null).collect(Collectors.toList());
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

}
