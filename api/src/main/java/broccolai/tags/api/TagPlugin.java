package broccolai.tags.api;

import broccolai.tags.api.model.Service;
import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagPlugin {

    private static @MonotonicNonNull Injector INJECTOR;

    private TagPlugin() {
    }

    public static void register(final @NonNull Injector injector) {
        INJECTOR = injector;
    }

    public static <T extends Service> T getService(final @NonNull Class<T> clazz) {
        return INJECTOR.getInstance(clazz);
    }

}
