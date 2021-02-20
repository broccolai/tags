package broccolai.tags.core.util;

import com.google.common.io.CharStreams;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class ResourceReader {

    private ResourceReader() {
    }

    public static @NonNull String readResource(final @NonNull String path) {
        try (InputStream stream = ResourceReader.class.getResourceAsStream("/" + path);
             Reader reader = new InputStreamReader(stream)) {
            return CharStreams.toString(reader);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
