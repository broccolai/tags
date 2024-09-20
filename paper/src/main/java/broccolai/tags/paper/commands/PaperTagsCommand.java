package broccolai.tags.paper.commands;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.tag.TagDisplayInformation;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.factory.CloudArgumentFactory;
import broccolai.tags.paper.commands.context.PaperPlayerCommandUser;
import broccolai.tags.paper.menu.TagsMenuFactory;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.interfaces.paper.PlayerViewer;

public final class PaperTagsCommand implements PluginCommand {

    private final UserService userService;
    private final CloudArgumentFactory argumentFactory;
    private final TagsMenuFactory tagsMenuFactory;

    @Inject
    public PaperTagsCommand(
            final @NonNull UserService userService,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull TagsMenuFactory tagsMenuFactory
    ) {
        this.userService = userService;
        this.argumentFactory = argumentFactory;
        this.tagsMenuFactory = tagsMenuFactory;
    }

    @Override
    public void register(@NonNull final CommandManager<@NonNull CommandUser> commandManager) {
        Command.Builder<CommandUser> tagsCommand = commandManager.commandBuilder("tags");

        commandManager.command(tagsCommand
                .literal("test-item")
                .permission("tags.command.admin.test-item")
                .required("tag", this.argumentFactory.tag(TagParserMode.SELF))
                .handler(this::handleTestItem)
        );

        commandManager.command(tagsCommand
                .literal("menu")
                .permission("tags.command.user.menu")
                .handler(this::handleMenu)
        );
    }

    private void handleTestItem(final @NonNull CommandContext<CommandUser> context) {
        ConstructedTag tag = context.get("tag");
        TagDisplayInformation displayInformation = tag.displayInformation();

        Material material = Material.matchMaterial(displayInformation.material());

        ItemStack item = PaperItemBuilder
                .ofType(material)
                .customModelData(displayInformation.customModelData())
                .build();

        PaperPlayerCommandUser user = context.sender().cast();
        user.player().getInventory().addItem(item);
    }

    private void handleMenu(final @NonNull CommandContext<CommandUser> context) {
        PaperPlayerCommandUser sender = context.sender().cast();
        TagsUser user = this.userService.get(sender.uuid());

        this.tagsMenuFactory.create(user).open(PlayerViewer.of(sender.player()));
    }

}
