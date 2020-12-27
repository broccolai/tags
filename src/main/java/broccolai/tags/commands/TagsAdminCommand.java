package broccolai.tags.commands;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TagsAdminCommand {

    private final @NonNull Permission permission;

    @Inject
    public TagsAdminCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull Permission permission
    ) {
        this.permission = permission;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tagsadmin");

        manager.command(tagsCommand
                .literal("give")
                .argument(argumentFactory.user("target"))
                .argument(argumentFactory.tag("tag", false, false))
                .handler(this::handleGive)
        );

        manager.command(tagsCommand
                .literal("remove")
                .argument(argumentFactory.user("target"))
                .argument(argumentFactory.tag("tag", false, false))
                .handler(this::handleRemove)
        );
    }

    private void handleGive(final @NonNull CommandContext<CommandUser> context) {
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target.uuid());

        this.permission.playerAdd(null, offlinePlayer, "tags.tag." + tag.id());
        context.getSender().sendMessage(Component.text("Tag " + tag.name() + " has been given to " + offlinePlayer.getName()));
    }

    private void handleRemove(final @NonNull CommandContext<CommandUser> context) {
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target.uuid());

        this.permission.playerRemove(null, offlinePlayer, "tags.tag." + tag.id());
        context.getSender().sendMessage(Component.text("Tag " + tag.name() + " has been removed from " + offlinePlayer.getName()));
    }

}
