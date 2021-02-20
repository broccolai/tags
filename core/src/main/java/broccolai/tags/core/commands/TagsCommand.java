package broccolai.tags.core.commands;

import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.factory.CloudArgumentFactory;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class TagsCommand {

    private final @NonNull MessageService messageService;
    private final @NonNull UserService userService;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull UserService userService,
            final @NonNull TagsService tagsService
    ) {
        this.messageService = messageService;
        this.userService = userService;
        this.tagsService = tagsService;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tags");

        manager.command(tagsCommand
                .literal("select")
                .permission("tags.command.user.select")
                .argument(argumentFactory.tag("tag", TagParserMode.SELF))
                .handler(this::handleSelect)
        );

        manager.command(tagsCommand
                .literal("list")
                .permission("tags.command.user.list")
                .handler(this::handleList)
        );

        manager.command(tagsCommand
                .literal("info")
                .permission("tags.command.user.info")
                .argument(argumentFactory.tag("tag", TagParserMode.NON_SECRET))
                .handler(this::handlePreview)
        );
    }

    private void handleSelect(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userService.get(sender.uuid());
        Tag tag = context.get("tag");

        user.setCurrent(tag);
        sender.sendMessage(this.messageService.commandSelect(tag));
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userService.get(sender.uuid());
        Collection<Tag> tags = this.tagsService.allTags(user);

        sender.sendMessage(this.messageService.commandList(tags));
    }

    private void handlePreview(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        Tag tag = context.get("tag");

        sender.sendMessage(this.messageService.commandInfo(tag));
    }

}
