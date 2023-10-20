package broccolai.tags.core.commands;

import broccolai.tags.api.events.event.TagChangeEvent;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.ActionService;
import broccolai.tags.api.service.EventService;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.factory.CloudArgumentFactory;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class TagsCommand implements PluginCommand {

    private final @NonNull CloudArgumentFactory argumentFactory;
    private final @NonNull MessageService messageService;
    private final @NonNull UserService userService;
    private final @NonNull TagsService tagsService;
    private final @NonNull ActionService actionService;

    @Inject
    public TagsCommand(
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull UserService userService,
            final @NonNull TagsService tagsService,
            final @NonNull ActionService actionService
    ) {
        this.argumentFactory = argumentFactory;
        this.messageService = messageService;
        this.userService = userService;
        this.tagsService = tagsService;
        this.actionService = actionService;
    }

    @Override
    public void register(
            @NonNull final CommandManager<@NonNull CommandUser> commandManager
    ) {
        Command.Builder<CommandUser> tagsCommand = commandManager.commandBuilder("tags");

        commandManager.command(tagsCommand
                .literal("select")
                .permission("tags.command.user.select")
                .argument(this.argumentFactory.tag("tag", TagParserMode.SELF))
                .handler(this::handleSelect)
        );

        commandManager.command(tagsCommand
                .literal("list")
                .permission("tags.command.user.list")
                .handler(this::handleList)
        );

        commandManager.command(tagsCommand
                .literal("info")
                .permission("tags.command.user.info")
                .argument(this.argumentFactory.tag("tag", TagParserMode.NON_SECRET))
                .handler(this::handlePreview)
        );
    }

    private void handleSelect(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userService.get(sender.uuid());
        ConstructedTag tag = context.get("tag");

        boolean success = this.actionService.select(user, tag);

        if (success) {
            sender.sendMessage(this.messageService.commandSelect(tag));
        }
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userService.get(sender.uuid());
        Collection<ConstructedTag> tags = this.tagsService.allTags(user);

        sender.sendMessage(this.messageService.commandList(tags));
    }

    private void handlePreview(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        ConstructedTag tag = context.get("tag");

        sender.sendMessage(this.messageService.commandInfo(tag));
    }

}
