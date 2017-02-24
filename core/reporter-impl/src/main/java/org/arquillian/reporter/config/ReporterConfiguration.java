package org.arquillian.reporter.config;

import java.util.Map;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterConfiguration {

    private  ReporterConfiguration(){
    }

    public static ReporterConfiguration fromMap(Map<String, String> reporterProps) {
        return new ReporterConfiguration();
    }

}
