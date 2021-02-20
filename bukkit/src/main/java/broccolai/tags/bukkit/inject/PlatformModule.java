package broccolai.tags.bukkit.inject;

import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.bukkit.TagsPlatform;
import broccolai.tags.bukkit.commands.context.BukkitCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitConsoleCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitPlayerCommandUser;
import broccolai.tags.bukkit.service.BukkitPipelineUserService;
import broccolai.tags.core.commands.arguments.TagArgument;
import broccolai.tags.core.commands.arguments.UserArgument;
import broccolai.tags.core.commands.context.CommandUser;
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
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public final class PlatformModule extends AbstractModule {

    private final @NonNull TagsPlatform plugin;

    public PlatformModule(final @NonNull TagsPlatform plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(TagsPlatform.class).toInstance(this.plugin);
        this.bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(this.plugin));
        this.bind(File.class).toInstance(this.plugin.getDataFolder());
        this.bind(UserService.class).to(BukkitPipelineUserService.class);
    }

    @Provides
    @Singleton
    CommandManager<CommandUser> provideCommandManager(
            final @NonNull Plugin plugin,
            final @NonNull BukkitAudiences audiences,
            final @NonNull MessageService messageService
    ) {
        try {
            PaperCommandManager<CommandUser> commandManager = new PaperCommandManager<>(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.<CommandUser>newBuilder().build(),
                    sender -> PlatformModule.from(sender, audiences),
                    user -> user.<BukkitCommandUser>cast().sender()
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
                user.sendMessage(messageService.commandErrorUserNotFound(ex.input()));
            });

            commandManager.registerExceptionHandler(TagArgument.TagArgumentException.class, (user, ex) -> {
                user.sendMessage(messageService.commandErrorTagNotFound(ex.input()));
            });

            return commandManager;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't start Command Manager");
        }
    }

    private static CommandUser from(final @NonNull CommandSender sender, final @NonNull BukkitAudiences audiences) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;

            return new BukkitConsoleCommandUser(console, audiences.console());
        } else if (sender instanceof Player) {
            Player player = (Player) sender;

            return new BukkitPlayerCommandUser(player, audiences.player(player));
        }

        return null;
    }

}
