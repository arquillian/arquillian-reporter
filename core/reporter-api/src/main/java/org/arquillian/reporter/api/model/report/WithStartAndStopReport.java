package org.arquillian.reporter.api.model.report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */

public interface WithStartAndStopReport {

    /**
     * Sets the given time as time when an associated execution started
     *
     * @param start Start time to be set
     */
    void setStart(String start);

    /**
     * Returns time when the execution started
     *
     * @return Time when the execution started
     */
    String getStart();

    /**
     * Returns time when the execution stopped
     *
     * @return Time when the execution stopped
     */
    String getStop();

    /**
     * Sets the given time as time when an associated execution stopped
     *
     * @param stop Stop time to be set
     */
    void setStop(String stop);

}
