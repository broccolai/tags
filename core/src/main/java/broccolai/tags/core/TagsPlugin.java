package broccolai.tags.core;

import broccolai.tags.api.TagsApi;
import broccolai.tags.api.events.EventListener;
import broccolai.tags.api.service.EventService;
import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.data.StorageMethod;
import broccolai.tags.core.service.user.partials.UserCacheService;
import broccolai.tags.core.subscribers.TagChangeSubscriber;
import broccolai.tags.core.util.ArrayUtilities;
import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

@Singleton
public final class TagsPlugin {

    private static final @NonNull Class<? extends EventListener>[] SUBSCRIBERS = ArrayUtilities.create(
            TagChangeSubscriber.class
    );

    private final @NonNull Injector injector;

    @Inject
    public TagsPlugin(final @NonNull Injector injector) {
        this.injector = injector;
    }

    public void start() {
        TagsApi.register(this.injector);

        EventService eventService = this.injector.getInstance(EventService.class);

        for (final Class<? extends EventListener> subscriber : SUBSCRIBERS) {
            eventService.register(this.injector.getInstance(subscriber));
        }
    }

    public void commands(
            final @NonNull CommandManager<@NonNull CommandUser> commandManager,
            final @NonNull Collection<Class<? extends PluginCommand>> commands
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
