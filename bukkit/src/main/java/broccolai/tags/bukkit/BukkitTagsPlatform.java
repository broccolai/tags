package broccolai.tags.bukkit;

import broccolai.tags.api.service.MessageService;
import broccolai.tags.bukkit.commands.BukkitTagsCommand;
import broccolai.tags.bukkit.commands.context.BukkitCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitConsoleCommandUser;
import broccolai.tags.bukkit.commands.context.BukkitPlayerCommandUser;
import broccolai.tags.bukkit.inject.PlatformModule;
import broccolai.tags.bukkit.inject.VaultModule;
import broccolai.tags.bukkit.integrations.BasicIntegration;
import broccolai.tags.bukkit.integrations.PapiIntegration;
import broccolai.tags.bukkit.listeners.PlayerListener;
import broccolai.tags.core.TagsPlugin;
import broccolai.tags.core.commands.PluginCommand;
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
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitTagsPlatform extends JavaPlugin implements TagsPlatform {

    private static final @NonNull Class<? extends Listener>[] BUKKIT_LISTENERS = ArrayUtilities.create(
            PlayerListener.class,
            BasicIntegration.class
    );

    private static final @NonNull Collection<Class<? extends PluginCommand>> BUKKIT_COMMANDS = ArrayUtilities.merge(
            COMMANDS,
            BukkitTagsCommand.class
    );

    private @MonotonicNonNull TagsPlugin plugin;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(ArrayUtilities.merge(
                STANDARD_MODULES,
                new PlatformModule(this),
                new VaultModule()
        ));

        this.plugin = injector.getInstance(TagsPlugin.class);
        this.plugin.start();

        CommandManager<CommandUser> commandManager = this.commandManager(
                injector.getInstance(MessageService.class)
        );

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            injector.getInstance(PapiIntegration.class).register();
        }

        for (final Class<? extends Listener> bukkitListener : BUKKIT_LISTENERS) {
            this.getServer().getPluginManager().registerEvents(
                    injector.getInstance(bukkitListener),
                    this
            );
        }

        this.plugin.commands(commandManager, BUKKIT_COMMANDS);
    }

    @Override
    public void onDisable() {
        this.plugin.shutdown();
    }

    private CommandManager<CommandUser> commandManager(
            final @NonNull MessageService messageService
    ) {
        try {
            PaperCommandManager<CommandUser> commandManager = new PaperCommandManager<>(
                    this,
                    AsynchronousCommandExecutionCoordinator.<CommandUser>builder().build(),
                    BukkitTagsPlatform::from,
                    user -> user.<BukkitCommandUser>cast().sender()
            );

            if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }

            if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                commandManager.registerBrigadier();
            }

            new MinecraftExceptionHandler<CommandUser>()
                    .withDefaultHandlers()
                    .apply(commandManager, e -> e);

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

    private static CommandUser from(final @NonNull CommandSender sender) {
        if (sender instanceof ConsoleCommandSender console) {
            return new BukkitConsoleCommandUser(console);
        } else if (sender instanceof Player player) {
            return new BukkitPlayerCommandUser(player);
        }

        return null;
    }

}
