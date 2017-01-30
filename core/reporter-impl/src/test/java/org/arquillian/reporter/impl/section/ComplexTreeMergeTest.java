package org.arquillian.reporter.impl.section;

import java.util.List;

import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.utils.Utils;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ComplexTreeMergeTest {


    public void createCopmlexSectionTree() throws InstantiationException, IllegalAccessException {

        List<TestSuiteReport> testSuiteReports =
            Utils.prepareSetOfReports(TestSuiteReport.class, 10, "test-suite", 1, 10);
    }

}
