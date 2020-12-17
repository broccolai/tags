package broccolai.tags.commands.arguments;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.service.tags.TagsService;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class TagArgument extends CommandArgument<@NonNull CommandUser, @NonNull Tag> {

    @AssistedInject
    public TagArgument(final @NonNull TagsService tagsService, final @Assisted("name") @NonNull String name) {
        super(true, name, new TagParser(tagsService), Tag.class);
    }

    public static final class TagParser implements ArgumentParser<@NonNull CommandUser, Tag> {

        private final @NonNull TagsService tagsService;

        public TagParser(final @NonNull TagsService tagsService) {
            this.tagsService = tagsService;
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

            if (tag == null) {
                return ArgumentParseResult.failure(new NullPointerException("Could not find tag with name " + input));
            }

            return ArgumentParseResult.success(tag);
        }

        @Override
        public @NonNull List<String> suggestions(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final String input
        ) {
            Collection<Tag> tags = this.tagsService.allTags();
            List<String> output = new ArrayList<>();

            for (Tag tag : tags) {
                output.add(tag.name());
            }

            return output;
        }

    }
}
