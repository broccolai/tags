package broccolai.tags.api;

import broccolai.tags.api.model.Service;
import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagsApi {

    private static @MonotonicNonNull Injector INJECTOR;

    private TagsApi() {
    }

    public static void register(final @NonNull Injector injector) {
        INJECTOR = injector;
    }

    public static <T extends Service> T service(final @NonNull Class<T> clazz) {
        return INJECTOR.getInstance(clazz);
    }

}
