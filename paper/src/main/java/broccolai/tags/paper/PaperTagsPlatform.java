package broccolai.tags.paper;

import broccolai.tags.api.service.MessageService;
import broccolai.tags.core.TagsPlugin;
import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.arguments.TagArgument;
import broccolai.tags.core.commands.arguments.UserArgument;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.platform.TagsPlatform;
import broccolai.tags.core.util.ArrayUtilities;
import broccolai.tags.paper.commands.PaperTagsCommand;
import broccolai.tags.paper.commands.context.PaperCommandUser;
import broccolai.tags.paper.commands.context.PaperConsoleCommandUser;
import broccolai.tags.paper.commands.context.PaperPlayerCommandUser;
import broccolai.tags.paper.inject.PlatformModule;
import broccolai.tags.paper.inject.VaultModule;
import broccolai.tags.paper.integrations.BasicIntegration;
import broccolai.tags.paper.integrations.MiniIntegration;
import broccolai.tags.paper.integrations.PapiIntegration;
import broccolai.tags.paper.listeners.PlayerListener;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.PaperInterfaceListeners;

public final class PaperTagsPlatform extends JavaPlugin implements TagsPlatform {

    private static final @NonNull Class<? extends Listener>[] PAPER_LISTENERS = ArrayUtilities.create(
            PlayerListener.class,
            BasicIntegration.class
    );

    private static final @NonNull Collection<Class<? extends PluginCommand>> PAPER_COMMANDS = ArrayUtilities.merge(
            COMMANDS,
            PaperTagsCommand.class
    );

    private @MonotonicNonNull TagsPlugin plugin;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        PaperInterfaceListeners.install(this);

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

        this.registerIntegrations(injector);

        for (final Class<? extends Listener> paperListener : PAPER_LISTENERS) {
            this.getServer().getPluginManager().registerEvents(
                    injector.getInstance(paperListener),
                    this
            );
        }

        this.plugin.commands(commandManager, PAPER_COMMANDS);
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
                    PaperTagsPlatform::from,
                    user -> user.<PaperCommandUser>cast().sender()
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

    //todo(josh): genericize this
    private void registerIntegrations(final Injector injector) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            injector.getInstance(PapiIntegration.class).register();
        }

        if (pluginManager.isPluginEnabled("MiniPlaceholders")) {
            injector.getInstance(MiniIntegration.class).registerExpansion();
        }
    }

    private static CommandUser from(final @NonNull CommandSender sender) {
        if (sender instanceof ConsoleCommandSender console) {
            return new PaperConsoleCommandUser(console);
        } else if (sender instanceof Player player) {
            return new PaperPlayerCommandUser(player);
        }

        return null;
    }

}
