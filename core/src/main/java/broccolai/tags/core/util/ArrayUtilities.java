package broccolai.tags.core.util;

import com.google.inject.Module;

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

    public static Collection<Module> merge(final @NonNull Module[] initial, final @NonNull Module... extra) {
        Deque<Module> modules = new ArrayDeque<>(Arrays.asList(initial));

        for (final Module module : extra) {
            modules.addFirst(module);
        }

        return modules;
    }

}
