package org.arquillian.reporter.api.model.entry;

import java.nio.file.Path;

/**
 * An {@link Entry} representing a file
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FileEntry implements Entry {

    private Path path;

    public FileEntry(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
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
