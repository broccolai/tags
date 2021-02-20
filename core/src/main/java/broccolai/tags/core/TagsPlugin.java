package broccolai.tags.core;

import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.data.StorageType;
import broccolai.tags.core.commands.TagsAdminCommand;
import broccolai.tags.core.commands.TagsCommand;
import broccolai.tags.core.data.jdbi.UserMapper;
import broccolai.tags.core.inject.ConfigurationModule;
import broccolai.tags.core.inject.DatabaseModule;
import broccolai.tags.core.inject.ServiceModule;
import broccolai.tags.core.inject.factory.CloudArgumentFactoryModule;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.core.service.user.partials.UserCacheService;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import java.io.File;

@Singleton
public final class TagsPlugin {

    private @MonotonicNonNull Injector injector;
    private @MonotonicNonNull HikariDataSource hikariDataSource;
    private @MonotonicNonNull Jdbi jdbi;

    public @NonNull Injector preload(final @NonNull Injector parent) {
        this.injector = parent.createChildInjector(
                new ConfigurationModule(),
                new DatabaseModule(this.jdbi),
                new ServiceModule(),
                new CloudArgumentFactoryModule()
        );

        final File folder = this.injector.getInstance(File.class);
        final MainConfiguration mainConfiguration = this.injector.getInstance(MainConfiguration.class);
        final HikariConfig hikariConfig = new HikariConfig();

        if (mainConfiguration.storage.storageType == StorageType.SQLITE) {
            File file = new File(folder, "tags.db");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + file.toString());
        }

        hikariConfig.setMaximumPoolSize(10);
        this.hikariDataSource = new HikariDataSource(hikariConfig);

        this.jdbi = Jdbi.create(this.hikariDataSource)
                .registerRowMapper(TagsUser.class, new UserMapper());

        Flyway.configure(this.getClass().getClassLoader())
                .baselineOnMigrate(true)
                .locations("classpath:queries/migrations")
                .dataSource(this.hikariDataSource)
                .load()
                .migrate();

        this.injector.getInstance(TagsCommand.class);
        this.injector.getInstance(TagsAdminCommand.class);

        return this.injector;
    }

    public void shutdown() {
        this.injector.getInstance(UserCacheService.class).close();

        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
    }

}
