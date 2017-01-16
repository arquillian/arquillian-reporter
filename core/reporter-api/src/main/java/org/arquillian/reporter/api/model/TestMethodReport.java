package org.arquillian.reporter.api.model;

import java.util.Date;

import org.arquillian.reporter.api.utils.TestMethodSectionBuilderImpl;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends AbstractSectionReport<TestMethodReport, TestMethodSectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private TestResult.Status status;
    private Failure failure = new Failure("Failures");
    private Configuration configuration = new Configuration();

    public TestMethodReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure failure) {
        this.failure = failure;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public TestMethodReport merge(TestMethodReport newSection) {
        if (newSection == null){
            return this;
        }
        defaultMerge(newSection);

        if (newSection.getStop() != null){
            setStop(newSection.getStop());
        }

        getConfiguration().merge(newSection.getConfiguration());
        getFailure().merge(newSection.getFailure());

        if (newSection.getStatus() != null){
            setStatus(newSection.getStatus());
        }

        return this;
    }

    @Override
    public TestMethodSectionBuilderImpl getSectionBuilderClass() {
        return new TestMethodSectionBuilderImpl(this);
    }

}
