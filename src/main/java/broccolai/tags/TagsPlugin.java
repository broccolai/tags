package broccolai.tags;

import broccolai.tags.commands.TagsAdminCommand;
import broccolai.tags.commands.TagsCommand;
import broccolai.tags.config.Configuration;
import broccolai.tags.config.LocaleConfiguration;
import broccolai.tags.config.serializers.LocaleEntrySerializer;
import broccolai.tags.data.StorageType;
import broccolai.tags.data.jdbi.UserMapper;
import broccolai.tags.inject.CloudModule;
import broccolai.tags.inject.PluginModule;
import broccolai.tags.inject.ServiceModule;
import broccolai.tags.inject.UserModule;
import broccolai.tags.inject.VaultModule;
import broccolai.tags.inject.factory.CloudArgumentFactoryModule;
import broccolai.tags.integrations.PapiIntegration;
import broccolai.tags.integrations.VaultIntegration;
import broccolai.tags.model.locale.LocaleEntry;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.user.impl.UserCacheService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;

@Singleton
public final class TagsPlugin extends JavaPlugin {

    private @MonotonicNonNull Injector injector;

    private @MonotonicNonNull Configuration configuration;
    private @MonotonicNonNull LocaleConfiguration localeConfiguration;
    private @MonotonicNonNull Jdbi jdbi;
    private @MonotonicNonNull HikariDataSource hikariDataSource;

    @Override
    public void onEnable() {
        try {
            this.configuration = this.loadConfiguration();
            this.localeConfiguration = this.loadLocaleConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.injector = Guice.createInjector(
                new PluginModule(this),
                new VaultModule(),
                new ServiceModule(),
                new CloudModule(),
                new UserModule(),
                new CloudArgumentFactoryModule()
        );

        final HikariConfig hikariConfig = new HikariConfig();

        if (this.configuration.storage.storageType == StorageType.SQLITE) {
            File file = new File(this.getDataFolder(), "tags.db");
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
        this.injector.getInstance(VaultIntegration.class);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.injector.getInstance(PapiIntegration.class).register();
        }
    }

    @Override
    public void onDisable() {
        this.injector.getInstance(UserCacheService.class).close();

        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
    }

    public @NonNull Jdbi getJdbi() {
        return this.jdbi;
    }

    public @NonNull Configuration getConfiguration() {
        return this.configuration;
    }

    public @NonNull LocaleConfiguration getLocaleConfiguration() {
        return this.localeConfiguration;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private @NonNull Configuration loadConfiguration() throws IOException {
        File file = new File(this.getDataFolder(), "config.conf");
        this.getDataFolder().mkdirs();
        file.createNewFile();

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .file(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        Configuration config = Configuration.loadFrom(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private @NonNull LocaleConfiguration loadLocaleConfiguration() throws IOException {
        File file = new File(this.getDataFolder(), "locale.conf");
        this.getDataFolder().mkdirs();
        file.createNewFile();

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(opts -> {
                    opts = opts.serializers(builder -> {
                        builder.register(LocaleEntry.class, LocaleEntrySerializer.INSTANCE);
                    });

                    return opts.shouldCopyDefaults(true);
                })
                .file(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        LocaleConfiguration config = LocaleConfiguration.loadFrom(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

}
