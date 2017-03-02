package org.arquillian.reporter.api.model.entry;

import java.io.File;
import java.nio.file.Path;

/**
 * An {@link Entry} representing a file
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FileEntry implements Entry {

    private String path;

    /**
     * Build object by setting file path to Path.toString().
     * @param path file.
     */
    public FileEntry(Path path) {
        this.path = path.toString();
    }

    /**
     * Build object by setting file path.
     * @param path file.
     */
    public FileEntry(String path) {
        this.path = path;
    }

    /**
     * Build object by setting file path to File.getPath().
     * @param path file.
     */
    public FileEntry(File path) {
        this.path = path.getPath();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntry fileEntry = (FileEntry) o;

        return path != null ? path.equals(fileEntry.path) : fileEntry.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
