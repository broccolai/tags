package broccolai.tags.bukkit;

import broccolai.tags.api.service.MessageService;
import broccolai.tags.bukkit.commands.context.BukkitCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitConsoleCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitPlayerCommandUser;
import broccolai.tags.bukkit.inject.PlatformModule;
import broccolai.tags.bukkit.inject.VaultModule;
import broccolai.tags.bukkit.integrations.PapiIntegration;
import broccolai.tags.bukkit.integrations.VaultIntegration;
import broccolai.tags.core.TagsPlugin;
import broccolai.tags.core.commands.arguments.TagArgument;
import broccolai.tags.core.commands.arguments.UserArgument;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.platform.TagsPlatform;
import broccolai.tags.core.util.ArrayUtilities;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitTagsPlatform extends JavaPlugin implements TagsPlatform {

    private @MonotonicNonNull TagsPlugin plugin;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(ArrayUtilities.merge(
                STANDARD_MODULES,
                new PlatformModule(this),
                new VaultModule()
        ));

        this.plugin = injector.getInstance(TagsPlugin.class);
        this.plugin.start();

        CommandManager<CommandUser> commandManager = this.getCommandManager(
                injector.getInstance(BukkitAudiences.class),
                injector.getInstance(MessageService.class)
        );

        this.getServer().getPluginManager().registerEvents(
                injector.getInstance(VaultIntegration.class),
                this
        );

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            injector.getInstance(PapiIntegration.class).register();
        }

        this.plugin.commands(commandManager, COMMANDS);
    }

    @Override
    public void onDisable() {
        this.plugin.shutdown();
    }

    private CommandManager<CommandUser> getCommandManager(
            final @NonNull BukkitAudiences audiences,
            final @NonNull MessageService messageService
    ) {
        try {
            PaperCommandManager<CommandUser> commandManager = new PaperCommandManager<>(
                    this,
                    AsynchronousCommandExecutionCoordinator.<CommandUser>newBuilder().build(),
                    sender -> from(sender, audiences),
                    user -> user.<BukkitCommandUser>cast().sender()
            );

            if (commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }

            if (commandManager.queryCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
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
            throw new RuntimeException(e);
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
