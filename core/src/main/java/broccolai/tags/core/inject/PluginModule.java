package broccolai.tags.core.inject;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.data.StorageMethod;
import broccolai.tags.core.data.jdbi.UserMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

public final class PluginModule extends AbstractModule {

    @Provides
    @Singleton
    public @NonNull HikariDataSource provideDataSource(
            final @NonNull File folder,
            final @NonNull MainConfiguration configuration
    ) throws IOException {
        HikariConfig hikariConfig = new HikariConfig();

        //todo(josh): cleanup
        if (configuration.storage.storageMethod == StorageMethod.H2) {
            Path file = folder.toPath().resolve("storage.db");
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
            hikariConfig.setDriverClassName("org.h2.Driver");
            hikariConfig.setJdbcUrl("jdbc:h2:" + file.toAbsolutePath() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE");
        }

        hikariConfig.setMaximumPoolSize(10);
        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public @NonNull Jdbi provideJdbi(
            final @NonNull HikariDataSource dataSource
    ) {
        return Jdbi.create(dataSource)
                .registerRowMapper(TagsUser.class, new UserMapper());
    }

}
