package org.arquillian.reporter.impl.utils;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;

import static org.arquillian.reporter.impl.asserts.SectionAssert.assertThatSection;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionGeneratorVerificationHelper {


    public static final int PARENT_COUNT_OF_COMPLEX_PREPARED_TREE =
        (int) (1  // test execution tree
            + (EXPECTED_NUMBER_OF_SECTIONS * 2) // test suite and test class trees
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2)); // test method trees

    public static final int TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE =
        (int) (PARENT_COUNT_OF_COMPLEX_PREPARED_TREE // number of parents
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2) * 2 // number of configs in test class and suite
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3) * 2); // number of configs and failures in method


    public static void verifyAllSectionsAreProcessed(Map<SectionEvent, List<? extends SectionEvent>> sections) {
        sections.keySet().stream().forEach(parent -> {
            sections.get(parent).stream().forEach(child -> assertThatSection(child).isProccessed());
        });
    }
}
