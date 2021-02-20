package broccolai.tags.core.commands;

import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.factory.CloudArgumentFactory;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TagsService;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;

import java.util.Collection;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagsAdminCommand {

    private final @NonNull PermissionService permissionService;
    private final @NonNull MessageService messageService;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsAdminCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull PermissionService permissionService,
            final @NonNull MessageService messageService,
            final @NonNull TagsService tagsService
    ) {
        this.permissionService = permissionService;
        this.messageService = messageService;
        this.tagsService = tagsService;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tagsadmin")
                .permission("tags.command.admin");

        manager.command(tagsCommand
                .literal("give")
                .permission("tags.command.admin.give")
                .argument(argumentFactory.user("target", true))
                .argument(argumentFactory.tag("tag", TagParserMode.ANY))
                .handler(this::handleGive)
        );

        manager.command(tagsCommand
                .literal("remove")
                .permission("tags.command.admin.remove")
                .argument(argumentFactory.user("target", true))
                .argument(argumentFactory.tag("tag", TagParserMode.ANY))
                .handler(this::handleRemove)
        );

        manager.command(tagsCommand
                .literal("list")
                .permission("tags.command.admin.list")
                .argument(argumentFactory.user("target", false))
                .handler(this::handleList)
        );
    }

    private void handleGive(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");

        this.permissionService.grant(target, tag);
        sender.sendMessage(this.messageService.commandAdminGive(tag, target));
    }

    private void handleRemove(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");

        this.permissionService.remove(target, tag);
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
