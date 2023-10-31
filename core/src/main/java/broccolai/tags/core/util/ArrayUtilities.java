package broccolai.tags.core.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ArrayUtilities {

    private ArrayUtilities() {
        // utility class
    }

    @SafeVarargs
    public static <@NonNull T> @NonNull T @NonNull [] create(final @NonNull T... entries) {
        return entries;
    }

    @SafeVarargs
    public static <T> Collection<T> merge(final @NonNull T[] initial, final @NonNull T... extra) {
        Deque<T> modules = new ArrayDeque<>(Arrays.asList(initial));

        for (final T module : extra) {
            modules.addFirst(module);
        }

        return modules;
    }

}
