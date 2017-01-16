package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arquillian.reporter.api.utils.SectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSection extends AbstractSection<TestSuiteSection,SectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestClassSection> testClassSections = new ArrayList<>();

    public TestSuiteSection(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<TestClassSection> getTestClassSections() {
        return testClassSections;
    }

    public TestSuiteSection stop() {
        this.stop = new Date(System.currentTimeMillis());
        return this;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    @Override
    public TestSuiteSection merge(TestSuiteSection newSection) {
        if (newSection == null){
            return this;
        }
        defaultMerge(newSection);

        getTestClassSections().addAll(newSection.getTestClassSections());

        if (newSection.getStop() != null){
            setStop(newSection.getStop());
        }


        getConfiguration().merge(newSection.getConfiguration());

        return this;
    }

    @Override
    public SectionBuilderImpl getSectionBuilderClass() {
        return new SectionBuilderImpl(this);
    }
}
