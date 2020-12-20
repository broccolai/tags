package broccolai.tags.inject;

import broccolai.tags.TagsPlugin;
import broccolai.tags.config.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

public final class PluginModule extends AbstractModule {

    private final @NonNull TagsPlugin plugin;

    public PluginModule(final @NonNull TagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(TagsPlugin.class).toInstance(this.plugin);
        this.bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(this.plugin));
        this.bind(Configuration.class).toInstance(this.plugin.getConfiguration());
    }

    @Provides
    @Singleton
    @NonNull Jdbi providesJdbi(final @NonNull TagsPlugin plugin) {
        return plugin.getJdbi();
    }

}
