package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.SectionTree;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.groups.Tuple;

import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.collectAllTrees;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdAndReportNameFromList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTreeAssert extends AbstractAssert<SectionTreeAssert, SectionTree> {

    public SectionTreeAssert(SectionTree actual) {
        super(actual, SectionTreeAssert.class);
    }

    public static SectionTreeAssert assertThatSectionTree(SectionTree actual) {
        return new SectionTreeAssert(actual);
    }

    public SectionTreeAssert hasRootIdentifier(Identifier identifier) {
        isNotNull();

        if (!Objects.equals(actual.getRootIdentifier(), identifier)) {
            failWithMessage("Expected root identifier of the section tree should be <%s> but was <%s>", identifier,
                            actual.getRootIdentifier());
        }
        return this;
    }

    public SectionTreeAssert hasAssociatedReport(Report report) {
        isNotNull();

        if (!Objects.equals(actual.getAssociatedReport(), report)) {
            failWithMessage("Expected associated report of the section tree should be <%s> but was <%s>", report,
                            actual.getAssociatedReport());
        }
        return this;
    }

    public SectionTreeAssert doesNotHaveAnySubtree() {
        isNotNull();
        assertThat(actual.getSubtrees()).as("The tree should not have any subtree").isEmpty();
        return this;
    }

    public ReportAssert associatedReport() {
        isNotNull();
        return new ReportAssert(actual.getAssociatedReport());
    }

    public SectionTreeAssert hasNumberOfSubTrees(int number) {
        isNotNull();
        if (actual.getSubtrees().size() != number) {
            failWithMessage("The expected number of subtrees should be <%s> but the actual is <%s>", number,
                            actual.getSubtrees().size());
        }
        return this;
    }

    public SectionTreeAssert hasSubtreeMatchingSection(SectionEvent section) {

        SectionTree subtreeWithIdentifier = getSubtreeWithIdentifier(getSectionIdentifier(section));

        // and check associated report
        assertThatSectionTree(subtreeWithIdentifier)
            .as("The subtree with same identifier that has the section <%s> should contain same associated report",
                section)
            .hasAssociatedReport(section.getReport());

        return this;
    }

    public SectionTreeAssert hasSubtreeWithIdentifier(Identifier identifier) {

        getSubtreeWithIdentifier(identifier);

        return this;
    }

    public SectionTreeAssert hasSubtreesMatchingSection(SectionEvent... sections) {

        Tuple[] expectedTuples = Arrays.stream(sections)
            .map(section -> tuple(getSectionIdentifier(section), section.getReport())).toArray(Tuple[]::new);

        assertThat(actual.getSubtrees())
            .usingRecursiveFieldByFieldElementComparator()
            .extracting("rootIdentifier", "associatedReport")
            .as("The subtree with identifier <%s> should contain only subtrees with identifiers and associated reports given <%s>",
                actual.getRootIdentifier(), expectedTuples)
            .contains(expectedTuples);

        return this;
    }

    public SectionTreeAssert wholeTreeConsistOfCouplesMatching(
        Map<? extends SectionEvent, List<? extends SectionEvent>> mapOfParentsAndListsOfChildren) {

        // get all trees within the whole tree
        List<SectionTree> actualListOfTrees = new ArrayList<>();
        collectAllTrees(actual, actualListOfTrees);

        mapOfParentsAndListsOfChildren.keySet().stream().forEach(parent -> {
            // get identifier of the parent section - this one we will be looking for
            Identifier parentIdentifier = getSectionIdentifier(parent);

            // check if the list of all trees is a tree with identifier that is same as the parent's one
            assertThat(actualListOfTrees)
                .usingRecursiveFieldByFieldElementComparator()
                .extracting("rootIdentifier", "associatedReport")
                .as("the whole subtree should contain only one occurrence of a tree containing identifier <%s>. ",
                    parentIdentifier)
                .containsOnlyOnce(new Tuple(parentIdentifier, parent.getReport()));

            // get the tree that corresponds to the parent section (has same identifier)
            SectionTree matched =
                getTreeWithIdAndReportNameFromList(actualListOfTrees, parentIdentifier, parent.getReport().getName())
                    .get();

            // get all section-children of the parent section
            List<? extends SectionEvent> children = mapOfParentsAndListsOfChildren.get(parent);

            // check that the found parent tree has all expected subtrees
            assertThatSectionTree(matched).hasSubtreesMatchingSection(children.stream().toArray(SectionEvent[]::new));

            // go through all section children, check that the corresponding tree is contained within the list of subtrees of the parent tree
            // and for those one that are not between the parents remove from the comprehensive list as they won't be processed
            children.stream()
                .forEach(child -> {
                    Identifier childIdentifier = getSectionIdentifier(child);
                    assertThat(matched.getSubtrees())
                        .usingRecursiveFieldByFieldElementComparator()
                        .extracting("rootIdentifier", "associatedReport")
                        .as("The list of trees should contain one occurrence of subtree with identifier <%s> and associated reports <%s>",
                            childIdentifier, child.getReport())
                        .containsOnlyOnce(new Tuple(childIdentifier, child.getReport()));

                    if (!mapOfParentsAndListsOfChildren.containsKey(child)) {
                        actualListOfTrees.remove(
                            getTreeWithIdAndReportNameFromList(matched.getSubtrees(), childIdentifier,
                                                               child.getReport().getName()).get());
                    }
                });

            // remove the parent tree from the list of all trees - it is verified
            actualListOfTrees.remove(matched);
        });

        assertThat(actualListOfTrees)
            .as("all trees within the whole tree should be matched and removed from the list")
            .isEmpty();

        return this;
    }

    private SectionTree getSubtreeWithIdentifier(Identifier identifier) {
        List<SectionTree> actualSubtrees = actual.getSubtrees();
        // filter subtrees to get the one with same identifier
        List<SectionTree> foundSubtreesWithSameIdentifier =
            actualSubtrees.stream()
                .filter(tree -> tree.getRootIdentifier().equals(identifier))
                .collect(Collectors.toList());

        assertThat(foundSubtreesWithSameIdentifier)
            .as("The tree should have the subtree with identifier <%s> but it wasn't found", identifier)
            .isNotEmpty();

        assertThat(foundSubtreesWithSameIdentifier)
            .as("The tree should have the subtree with identifier <%s> only once but there were found <%s> occurrences",
                identifier, foundSubtreesWithSameIdentifier.size())
            .hasSize(1);

        return foundSubtreesWithSameIdentifier.get(0);
    }

    public SectionTreeAssert wholeTreeHasNumberOfTreeNodes(int number) {
        isNotNull();
        ArrayList<SectionTree> sectionTrees = new ArrayList<>();
        collectAllTrees(actual, sectionTrees);
        assertThat(sectionTrees).as("number of all collected tree-nodes should be %s", number).hasSize(number);
        return this;
    }

}
