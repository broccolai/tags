package broccolai.tags.commands;

import broccolai.corn.core.Lists;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class TagsCommand {

    private final @NonNull UserPipeline userPipeline;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull UserPipeline userPipeline,
            final @NonNull TagsService tagsService
    ) {
        this.userPipeline = userPipeline;
        this.tagsService = tagsService;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tags");

        manager.command(tagsCommand
                .literal("select")
                .argument(argumentFactory.tag("tag", true, true))
                .handler(this::handleSelect)
        );

        manager.command(tagsCommand
                .literal("list")
                .handler(this::handleList)
        );

        manager.command(tagsCommand
                .literal("preview")
                .argument(argumentFactory.tag("tag", true, true))
                .handler(this::handlePreview)
        );
    }

    private void handleSelect(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userPipeline.get(sender.uniqueId());
        Tag tag = context.get("tag");
        Component message = Component.text("You have selected the " + tag.name() + " prefix");

        user.setCurrent(tag);
        sender.sendMessage(message);
    }

    private void handleList(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        TagsUser user = this.userPipeline.get(sender.uniqueId());
        Collection<Tag> tags = this.tagsService.allTags(user);
        Component message = Component.join(Component.text(", "), Lists.map(tags, Tag::component));

        sender.sendMessage(message);
    }

    private void handlePreview(final @NonNull CommandContext<CommandUser> context) {
        CommandUser sender = context.getSender();
        Tag tag = context.get("tag");
        Component message = Component.text("The " + tag.name() + " prefix will look like this: ").append(tag.component());

        sender.sendMessage(message);
    }

}
