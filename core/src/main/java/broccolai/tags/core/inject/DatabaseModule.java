package broccolai.tags.core.inject;

import com.google.inject.AbstractModule;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

public final class DatabaseModule extends AbstractModule {

    public DatabaseModule(final @NonNull Jdbi jdbi) {
        this.bind(Jdbi.class).toInstance(jdbi);
    }
}
