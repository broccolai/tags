package broccolai.tags.core.commands;

import broccolai.tags.api.events.event.TagChangeEvent;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.EventService;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.core.commands.arguments.UserParser;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import broccolai.tags.core.factory.CloudArgumentFactory;
import com.google.inject.Inject;
import java.util.Collection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

public final class TagsAdminCommand implements PluginCommand {

    private final @NonNull CloudArgumentFactory argumentFactory;
    private final @NonNull PermissionService permissionService;
    private final @NonNull MessageService messageService;
    private final @NonNull EventService eventService;
    private final @NonNull TagsService tagsService;
    private final @NonNull UserParser userParser;

    @Inject
    public TagsAdminCommand(
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull PermissionService permissionService,
            final @NonNull MessageService messageService,
            final @NonNull EventService eventService,
            final @NonNull TagsService tagsService,
            final @NonNull UserParser userParser
    ) {
        this.argumentFactory = argumentFactory;
        this.permissionService = permissionService;
        this.messageService = messageService;
        this.eventService = eventService;
        this.tagsService = tagsService;
        this.userParser = userParser;
    }

    @Override
    public void register(
            final @NonNull CommandManager<@NonNull CommandUser> commandManager
    ) {
        Command.Builder<CommandUser> tagsCommand = commandManager.commandBuilder("tagsadmin")
                .permission("tags.command.admin");

        commandManager.command(tagsCommand
                .literal("give")
                .permission("tags.command.admin.give")
                .required("target", this.userParser)
                .required("tag", this.argumentFactory.tag(TagParserMode.ANY))
                .handler(this::handleGive)
        );

        commandManager.command(tagsCommand
                .literal("remove")
                .permission("tags.command.admin.remove")
                .required("target", this.userParser)
                .required("tag", this.argumentFactory.tag(TagParserMode.ANY))
                .handler(this::handleRemove)
        );

        commandManager.command(tagsCommand
                .literal("list")
                .permission("tags.command.admin.list")
                .optional("target", this.userParser)
                .handler(this::handleList)
        );

        commandManager.command(tagsCommand
                .literal("set")
                .permission("tags.command.admin.set")
                .required("target", this.userParser)
                .required("tag", this.argumentFactory.tag(TagParserMode.TARGET))
                .handler(this::handleSet)
        );
    }

    private void handleGive(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.sender();
        TagsUser target = context.get("target");
        ConstructedTag tag = context.get("tag");

        this.permissionService.grant(target, tag);
        sender.sendMessage(this.messageService.commandAdminGive(tag, target));
    }

    private void handleRemove(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.sender();
        TagsUser target = context.get("target");
        ConstructedTag tag = context.get("tag");

        this.permissionService.remove(target, tag);
        sender.sendMessage(this.messageService.commandAdminRemove(tag, target));
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.sender();
        Collection<ConstructedTag> tags = context.<TagsUser>optional("target")
                .map(this.tagsService::allTags)
                .orElse(this.tagsService.allTags());

        sender.sendMessage(this.messageService.commandAdminList(tags));
    }

    private void handleSet(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.sender();
        TagsUser target = context.get("target");
        ConstructedTag tag = context.get("tag");

        TagChangeEvent event = new TagChangeEvent(target, tag);
        this.eventService.post(event);

        if (!event.cancelled()) {
            sender.sendMessage(this.messageService.commandAdminSet(tag, target));
        }
    }

}
