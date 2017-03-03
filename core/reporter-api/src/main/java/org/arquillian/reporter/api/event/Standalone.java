package org.arquillian.reporter.api.event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Standalone {

    static String getStandaloneId(){
        return Standalone.class + "_section_containing_standalone_report";
    }
}
