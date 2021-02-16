package broccolai.tags.commands;

import broccolai.tags.commands.arguments.modes.TagParserMode;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.message.MessageService;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class TagsCommand {

    private final @NonNull MessageService messageService;
    private final @NonNull UserPipeline userPipeline;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull UserPipeline userPipeline,
            final @NonNull TagsService tagsService
    ) {
        this.messageService = messageService;
        this.userPipeline = userPipeline;
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
        TagsUser user = this.userPipeline.get(sender.uuid());
        Tag tag = context.get("tag");

        user.setCurrent(tag);
        sender.sendMessage(this.messageService.commandSelect(tag));
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userPipeline.get(sender.uuid());
        Collection<Tag> tags = this.tagsService.allTags(user);

        sender.sendMessage(this.messageService.commandList(tags));
    }

    private void handlePreview(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        Tag tag = context.get("tag");

        sender.sendMessage(this.messageService.commandInfo(tag));
    }

}
