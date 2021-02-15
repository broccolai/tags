package broccolai.tags.inject;

import broccolai.tags.commands.arguments.TagArgument;
import broccolai.tags.commands.arguments.UserArgument;
import broccolai.tags.commands.context.CommandUser;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

            if (commandManager.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
                commandManager.registerBrigadier();
            }

            new MinecraftExceptionHandler<CommandUser>()
                    .withDefaultHandlers()
                    .apply(commandManager, ForwardingAudience.Single::audience);

            commandManager.registerExceptionHandler(UserArgument.UserArgumentException.class, (user, ex) -> {
                user.sendMessage(Component.text("User not found for: " + ex.input(), NamedTextColor.RED));
            });

            commandManager.registerExceptionHandler(TagArgument.TagArgumentException.class, (user, ex) -> {
                user.sendMessage(Component.text("Tag not found for: " + ex.input(), NamedTextColor.RED));
            });

            return commandManager;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't start Command Manager");
        }
    }

}
