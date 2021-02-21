package broccolai.tags.core;

import broccolai.tags.api.TagsApi;
import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.TagsAdminCommand;
import broccolai.tags.core.commands.TagsCommand;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.data.StorageMethod;
import broccolai.tags.core.service.user.partials.UserCacheService;
import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class TagsPlugin {

    private final @NonNull Injector injector;

    @Inject
    public TagsPlugin(final @NonNull Injector injector) {
        this.injector = injector;
    }

    public void start() {
        TagsApi.register(this.injector);

        this.injector.getInstance(TagsCommand.class);
        this.injector.getInstance(TagsAdminCommand.class);
    }

    public void commands(
            final @NonNull CommandManager<@NonNull CommandUser> commandManager,
            final @NonNull Class<? extends PluginCommand>[] commands
    ) {
        for (final @NonNull Class<? extends PluginCommand> commandClass : commands) {
            this.injector.getInstance(commandClass).register(commandManager);
        }
    }

    public void shutdown() {
        this.injector.getInstance(UserCacheService.class).close();

        if (this.injector.getInstance(MainConfiguration.class).storage.storageMethod != StorageMethod.LUCKPERMS) {
            this.injector.getInstance(HikariDataSource.class).close();
        }
    }


}
