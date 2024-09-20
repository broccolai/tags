package broccolai.tags.paper;

import broccolai.tags.api.service.MessageService;
import broccolai.tags.core.TagsPlugin;
import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.arguments.TagParser;
import broccolai.tags.core.commands.arguments.UserParser;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.platform.TagsPlatform;
import broccolai.tags.core.util.ArrayUtilities;
import broccolai.tags.paper.commands.PaperTagsCommand;
import broccolai.tags.paper.commands.context.PaperCommandUser;
import broccolai.tags.paper.commands.context.PaperConsoleCommandUser;
import broccolai.tags.paper.commands.context.PaperPlayerCommandUser;
import broccolai.tags.paper.inject.PlatformModule;
import broccolai.tags.paper.inject.VaultModule;
import broccolai.tags.paper.integrations.MiniIntegration;
import broccolai.tags.paper.integrations.PapiIntegration;
import broccolai.tags.paper.listeners.PlayerListener;
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
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.interfaces.paper.PaperInterfaceListeners;

public final class PaperTagsPlatform extends JavaPlugin implements TagsPlatform {

    private static final @NonNull Class<? extends Listener>[] PAPER_LISTENERS = ArrayUtilities.create(
            PlayerListener.class
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
            LegacyPaperCommandManager<CommandUser> commandManager = new LegacyPaperCommandManager<>(
                    this,
                    ExecutionCoordinator.<CommandUser>builder()
                        .suggestionsExecutor(task ->
                            this.getServer().getScheduler().runTaskAsynchronously(this, b -> task.run()))
                        .build(),
                    SenderMapper.create(
                        PaperTagsPlatform::from,
                        user -> user.<PaperCommandUser>cast().sender()
                    )
            );

            if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                commandManager.registerBrigadier();
            } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }

            MinecraftExceptionHandler.<CommandUser>createNative()
                    .defaultHandlers()
                    .registerTo(commandManager);

            commandManager.exceptionController().registerHandler(UserParser.UserParseException.class, ctx -> {
                ctx.context().sender().sendMessage(messageService.commandErrorUserNotFound(ctx.exception().input()));
            });

            commandManager.exceptionController().registerHandler(TagParser.TagParseException.class, ctx -> {
                ctx.context().sender().sendMessage(messageService.commandErrorTagNotFound(ctx.exception().input()));
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
