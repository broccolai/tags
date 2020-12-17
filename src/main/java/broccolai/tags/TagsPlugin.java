package broccolai.tags;

import broccolai.tags.commands.TagsCommand;
import broccolai.tags.config.Configuration;
import broccolai.tags.data.jdbi.TagsColumnMapper;
import broccolai.tags.data.jdbi.UserMapper;
import broccolai.tags.inject.CloudModule;
import broccolai.tags.inject.DataModule;
import broccolai.tags.inject.PluginModule;
import broccolai.tags.inject.UserModule;
import broccolai.tags.inject.factory.CloudArgumentFactoryModule;
import broccolai.tags.integrations.TagsPlaceholders;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.user.impl.UserCacheService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;

@Singleton
public final class TagsPlugin extends JavaPlugin {

    private Injector injector;

    private Configuration configuration;
    private Jdbi jdbi;
    private HikariDataSource hikariDataSource;

    @Override
    public void onEnable() {
        try {
            this.configuration = this.loadConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.injector = Guice.createInjector(
                new PluginModule(this),
                new DataModule(),
                new CloudModule(),
                new UserModule(),
                new CloudArgumentFactoryModule()
        );

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.configuration.sql.jdbcUri);
        hikariConfig.setUsername(this.configuration.sql.username);
        hikariConfig.setPassword(this.configuration.sql.password);
        hikariConfig.setMaximumPoolSize(this.configuration.sql.maxConnections);
        this.hikariDataSource = new HikariDataSource(hikariConfig);

        this.jdbi = Jdbi.create(this.hikariDataSource)
                .registerRowMapper(TagsUser.class, new UserMapper())
                .registerColumnMapper(this.injector.getInstance(TagsColumnMapper.class));

        Flyway.configure(this.getClass().getClassLoader())
                .baselineOnMigrate(true)
                .locations("classpath:queries/migrations")
                .dataSource(this.hikariDataSource)
                .load()
                .migrate();

        this.injector.getInstance(TagsCommand.class);
        this.injector.getInstance(TagsPlaceholders.class).register();
    }

    @Override
    public void onDisable() {
        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }

        this.injector.getInstance(UserCacheService.class).close();
    }

    public Jdbi getJdbi() {
        return this.jdbi;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Configuration loadConfiguration() throws IOException {
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

}
