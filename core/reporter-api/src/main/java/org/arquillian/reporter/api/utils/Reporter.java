package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.AbstractSectionReport;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    public static SectionBuilder section(String name){
        return new SectionBuilderImpl(new SectionReport(name));
    }

    public static <T extends SectionBuilder, S extends Section<? extends AbstractSectionReport, T>> T section(S sectionReport){

        return sectionReport.getSectionBuilderClass();
        //        try {
//            Constructor<?> constructor = sectionBuilderClass.getConstructor(SectionReport.class);
//            return (T) constructor.newInstance((S) sectionReport);
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        throw new IllegalArgumentException();
    }

    public static FireSection fireSection(SectionReport sectionReport){
        return new FireSectionImpl(sectionReport);
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
