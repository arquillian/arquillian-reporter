package org.arquillian.reporter.api.model.report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */

public interface WithStartAndStopReport {

    /**
     * Sets the given time as time when an associated execution started
     *
     * @param startTime Start time to be set
     */
    void setExecutionStartTime(String startTime);

    /**
     * Returns time when the execution started
     *
     * @return Time when the execution started
     */
    String getExecutionStartTime();

    /**
     * Returns time when the execution stopped
     *
     * @return Time when the execution stopped
     */
    String getExecutionStopTime();

    /**
     * Sets the given time as time when an associated execution stopped
     *
     * @param stopTime Stop time to be set
     */
    void setExecutionStopTime(String stopTime);

}
