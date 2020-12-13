package broccolai.tags.commands;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.TagsUser;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public final class TagsCommand {

    private final UserPipeline userPipeline;

    @Inject
    public TagsCommand(final CommandManager<CommandUser> manager, final UserPipeline userPipeline) {
        this.userPipeline = userPipeline;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tags");

        manager.command(tagsCommand.handler(this::handleTags));
    }

    private void handleTags(final CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userPipeline.get(new UUID(0, 0));

        sender.sendMessage(Component.text(user.getClass().getSimpleName()));
    }

}
