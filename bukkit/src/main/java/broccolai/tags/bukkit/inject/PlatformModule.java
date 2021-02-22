package broccolai.tags.bukkit.inject;

import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TaskService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.bukkit.BukkitTagsPlatform;
import broccolai.tags.bukkit.service.BukkitPermissionService;
import broccolai.tags.bukkit.service.BukkitPipelineUserService;
import broccolai.tags.bukkit.service.BukkitTaskService;
import broccolai.tags.core.platform.TagsPlatform;
import com.google.inject.AbstractModule;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public final class PlatformModule extends AbstractModule {

    private final @NonNull BukkitTagsPlatform plugin;

    public PlatformModule(final @NonNull BukkitTagsPlatform plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(TagsPlatform.class).toInstance(this.plugin);
        this.bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(this.plugin));
        this.bind(File.class).toInstance(this.plugin.getDataFolder());
        this.bind(TaskService.class).to(BukkitTaskService.class);
        this.bind(UserService.class).to(BukkitPipelineUserService.class);
        this.bind(PermissionService.class).to(BukkitPermissionService.class);
    }

}
