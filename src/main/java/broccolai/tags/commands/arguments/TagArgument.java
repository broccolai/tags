package broccolai.tags.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class TagArgument extends CommandArgument<@NonNull CommandUser, @NonNull Tag> {

    @AssistedInject
    public TagArgument(
            final @NonNull TagsService tagsService,
            final @Assisted("name") @NonNull String name,
            final @Assisted("shouldCheck") boolean shouldCheck
    ) {
        super(true, name, new TagParser(tagsService, shouldCheck), Tag.class);
    }

    public static final class TagParser implements ArgumentParser<@NonNull CommandUser, Tag> {

        private final @NonNull TagsService tagsService;
        private final boolean shouldCheck;

        public TagParser(
                final @NonNull TagsService tagsService,
                final boolean shouldCheck
        ) {
            this.tagsService = tagsService;
            this.shouldCheck = shouldCheck;
        }

        @Override
        public @NonNull ArgumentParseResult<Tag> parse(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final Queue<String> inputQueue
        ) {
            final String input = inputQueue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NullPointerException("Expected tag name"));
            }

            Tag tag = this.tagsService.load(input);

            if (tag == null || !commandContext.getSender().hasPermission("tags.tag." + tag.id())) {
                return ArgumentParseResult.failure(new NullPointerException("Could not find tag with name " + input));
            }

            inputQueue.remove();
            return ArgumentParseResult.success(tag);
        }

        @Override
        public @NonNull List<String> suggestions(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final String input
        ) {
            Collection<Tag> tags;

            if (this.shouldCheck) {
                TagsUser target = commandContext.get("target");
                tags = this.tagsService.allTags(target);
            } else {
                tags = this.tagsService.allTags();
            }

            return Lists.map(tags, Tag::name);
        }

    }

}
