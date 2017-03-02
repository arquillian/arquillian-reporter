package org.arquillian.reporter.api.model.entry;

import java.io.File;
import java.nio.file.Path;

/**
 * An {@link Entry} representing a file
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FileEntry implements Entry {

    private String filePath;

    /**
     * Build object by setting file path to Path.toString().
     * @param path file.
     */
    public FileEntry(Path path) {
        this.filePath = path.toString();
    }

    /**
     * Build object by setting file path.
     * @param path file.
     */
    public FileEntry(String path) {
        this.filePath = path;
    }

    /**
     * Build object by setting file path to File.getPath().
     * @param path file.
     */
    public FileEntry(File path) {
        this.filePath = path.getPath();
    }

    public String getPath() {
        return filePath;
    }

    public void setPath(String path) {
        this.filePath = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntry fileEntry = (FileEntry) o;

        return filePath != null ? filePath.equals(fileEntry.filePath) : fileEntry.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
