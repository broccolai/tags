package broccolai.tags.paper.inject;

import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TaskService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.paper.PaperTagsPlatform;
import broccolai.tags.paper.service.PaperPermissionService;
import broccolai.tags.paper.service.PaperPipelineUserService;
import broccolai.tags.paper.service.PaperTaskService;
import broccolai.tags.core.platform.TagsPlatform;
import com.google.inject.AbstractModule;
import java.io.File;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public final class PlatformModule extends AbstractModule {

    private final @NonNull PaperTagsPlatform plugin;

    public PlatformModule(final @NonNull PaperTagsPlatform plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(TagsPlatform.class).toInstance(this.plugin);
        this.bind(File.class).toInstance(this.plugin.getDataFolder());
        this.bind(Logger.class).toInstance(this.plugin.getSLF4JLogger());
        this.bind(TaskService.class).to(PaperTaskService.class);
        this.bind(UserService.class).to(PaperPipelineUserService.class);
        this.bind(PermissionService.class).to(PaperPermissionService.class);
    }

}
