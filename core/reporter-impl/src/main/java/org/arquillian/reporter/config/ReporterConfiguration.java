package org.arquillian.reporter.config;

import java.io.File;
import java.util.Map;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterConfiguration {

    private static final String ROOT_DIRECTORY = "rootDirectory";
    private static final String FILE = "file";

    private String rootDirectory = "target";
    private String file = "report.json";

    private  ReporterConfiguration(){
    }

    public static ReporterConfiguration fromMap(Map<String, String> reporterProps) {
        final ReporterConfiguration reporterConfiguration = new ReporterConfiguration();

        if (reporterProps.containsKey(ROOT_DIRECTORY)) {
            reporterConfiguration.rootDirectory = reporterProps.get(ROOT_DIRECTORY);
        }

        if (reporterProps.containsKey(FILE)) {
            reporterConfiguration.file = reporterProps.get(FILE);
        }

        return reporterConfiguration;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getFile() {
        return file;
    }

    public File getReportFile() {
        return new File(rootDirectory, file);
    }
}
