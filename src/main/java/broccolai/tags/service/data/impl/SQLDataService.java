package broccolai.tags.service.data.impl;

import broccolai.tags.data.jdbi.UserReducer;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.data.DataService;
import broccolai.tags.util.ResourceReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;
import java.util.UUID;

@Singleton
public final class SQLDataService implements DataService {

    private static final String PATH = "queries/";

    private final Jdbi jdbi;

    @Inject
    public SQLDataService(final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public @NonNull Optional<@NonNull TagsUser> getUser(final @NonNull UUID uniqueId) {
        return this.jdbi.withHandle(handle -> handle
                .createQuery(Query.SELECT_USER.get())
                .reduceRows(new UserReducer())
                .findFirst()
                .map(TagsUser.Builder::build)
        );
    }

    private enum Query {
        SELECT_USER("select-user");

        private final @NonNull String query;

        Query(final @NonNull String name) {
            this.query = ResourceReader.readResource(PATH + name + ".sql");
        }

        public @NonNull String get() {
            return this.query;
        }
    }

}
