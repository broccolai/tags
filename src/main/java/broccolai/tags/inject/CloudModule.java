package broccolai.tags.inject;

import broccolai.tags.commands.context.CommandUser;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CloudModule extends AbstractModule {

    @Provides
    @Singleton
    CommandManager<CommandUser> provideCommandManager(final @NonNull Plugin plugin, final @NonNull BukkitAudiences audiences) {
        try {
            PaperCommandManager<CommandUser> commandManager = new PaperCommandManager<>(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.<CommandUser>newBuilder().build(),
                    sender -> CommandUser.from(sender, audiences),
                    CommandUser::sender
            );

            if (commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }

            return commandManager;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't start Command Manager");
        }
    }

}
