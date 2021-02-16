package broccolai.tags.commands;

import broccolai.tags.commands.arguments.modes.TagParserMode;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.message.MessageService;
import broccolai.tags.service.tags.TagsService;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;

import java.util.Collection;

import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagsAdminCommand {

    private final @NonNull Permission permission;
    private final @NonNull MessageService messageService;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsAdminCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull Permission permission,
            final @NonNull MessageService messageService,
            final @NonNull TagsService tagsService
    ) {
        this.permission = permission;
        this.messageService = messageService;
        this.tagsService = tagsService;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tagsadmin");

        manager.command(tagsCommand
                .literal("give")
                .argument(argumentFactory.user("target", true))
                .argument(argumentFactory.tag("tag", TagParserMode.ANY))
                .handler(this::handleGive)
        );

        manager.command(tagsCommand
                .literal("remove")
                .argument(argumentFactory.user("target", true))
                .argument(argumentFactory.tag("tag", TagParserMode.ANY))
                .handler(this::handleRemove)
        );

        manager.command(tagsCommand
                .literal("list")
                .argument(argumentFactory.user("target", false))
                .handler(this::handleList)
        );
    }

    private void handleGive(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");

        target.grant(this.permission, tag);
        sender.sendMessage(this.messageService.commandAdminGive(tag, target));
    }

    private void handleRemove(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");

        target.remove(this.permission, tag);
        sender.sendMessage(this.messageService.commandAdminRemove(tag, target));
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        Collection<Tag> tags = context.<TagsUser>getOptional("target")
                .map(this.tagsService::allTags)
                .orElse(this.tagsService.allTags());

        sender.sendMessage(this.messageService.commandAdminList(tags));
    }

}
