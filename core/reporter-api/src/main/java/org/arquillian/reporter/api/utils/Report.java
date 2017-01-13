package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Report {

    public static Section section(String name){
        return new SectionImpl(name);
    }

    public static Section modifySection(SectionReport sectionReport){
        return new SectionImpl(sectionReport);
    }

//    public static Section attachToSection(String identifier, Section section){
//        return attachToSection(identifier, section.build());
//    }
//
//
//    public static Section attachToSection(String identifier, SectionReport section){
//        SectionImpl parentSection = new SectionImpl(null);
//        parentSection.setIdentifier(identifier);
//        parentSection.addSection(section);
//        return parentSection;
//    }

//    public static Section attachTo(Report parentSection, Report section){
//        parentSection.addSection(section);
//        return parentSection;
//    }
}
