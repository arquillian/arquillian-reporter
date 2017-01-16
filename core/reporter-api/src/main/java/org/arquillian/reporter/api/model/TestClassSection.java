package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arquillian.reporter.api.utils.TestClassSectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSection extends AbstractSection<TestClassSection,TestClassSectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestMethodSection> testMethodSections = new ArrayList<>();

    public TestClassSection(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
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

    public List<TestMethodSection> getTestMethodSections() {
        return testMethodSections;
    }

    @Override
    public TestClassSection merge(TestClassSection newSection) {
        if (newSection == null) {
            return this;
        }
        defaultMerge(newSection);

        getTestMethodSections().addAll(newSection.getTestMethodSections());

        if (newSection.getStop() != null) {
            setStop(newSection.getStop());
        }

        getConfiguration().merge(newSection.getConfiguration());

        return this;
    }

    @Override
    public TestClassSectionBuilderImpl getSectionBuilderClass() {
        return new TestClassSectionBuilderImpl(this);
    }

}
