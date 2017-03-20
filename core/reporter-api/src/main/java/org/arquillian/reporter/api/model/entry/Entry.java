package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.model.report.Report;

/**
 * An interface representing an entry that could be contained in {@link Report}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Entry {

    /**
     * Returns an instance of an {@link Entry} implementation that will be added as final form of this entry into the
     * resulting report tree. This is a opportunity to process/modify data included in this entry or to change the
     * whole structure.
     *
     * @return An instance of an {@link Entry} implementation that will be added as final form of this entry into the
     * resulting report tree
     */
    Entry outputEntry();

}
