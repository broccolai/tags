package broccolai.tags.commands;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class TagsCommand {

    private final @NonNull UserPipeline userPipeline;

    @Inject
    public TagsCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull UserPipeline userPipeline
    ) {
        this.userPipeline = userPipeline;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tags");

        manager.command(tagsCommand
                .argument(argumentFactory.tag("tag"))
                .handler(this::handleTags));
    }

    private void handleTags(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userPipeline.get(new UUID(0, 0));
        Tag tag = context.get("tag");

        sender.sendMessage(Component.text(tag.name()));
    }

}
