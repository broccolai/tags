package broccolai.tags.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tags.commands.arguments.modes.TagParserMode;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.util.ArrayList;

import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class TagArgument extends CommandArgument<@NonNull CommandUser, @NonNull Tag> {

    @AssistedInject
    public TagArgument(
            final @NonNull TagsService tagsService,
            final @NonNull UserPipeline userPipeline,
            final @NonNull Permission permission,
            final @Assisted("name") @NonNull String name,
            final @Assisted("mode") @NonNull TagParserMode mode
    ) {
        super(true, name, new TagParser(tagsService, userPipeline, permission, mode), Tag.class);
    }

    public static final class TagParser implements ArgumentParser<@NonNull CommandUser, Tag> {

        private final @NonNull TagsService tagsService;
        private final @NonNull UserPipeline userPipeline;
        private final @NonNull Permission permission;
        private final @NonNull TagParserMode mode;

        public TagParser(
                final @NonNull TagsService tagsService,
                final @NonNull UserPipeline userPipeline,
                final @NonNull Permission permission,
                final @NonNull TagParserMode mode
        ) {
            this.tagsService = tagsService;
            this.userPipeline = userPipeline;
            this.permission = permission;
            this.mode = mode;
        }

        @Override
        public @NonNull ArgumentParseResult<Tag> parse(
                final @NonNull CommandContext<@NonNull CommandUser> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            final String input = inputQueue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(TagParser.class, commandContext));
            }

            Tag tag = this.tagsService.load(input);

            if (tag == null) {
                return ArgumentParseResult.failure(new TagArgumentException(input));
            }

            if (this.mode == TagParserMode.NON_SECRET && tag.secret()) {
                CommandUser self = commandContext.getSender();
                TagsUser user = this.userPipeline.get(self.uuid());

                if (!user.owns(this.permission, tag)) {
                    return ArgumentParseResult.failure(new TagArgumentException(input));
                }
            } else if (this.mode == TagParserMode.SELF || this.mode == TagParserMode.TARGET) {
                final TagsUser user;

                if (this.mode == TagParserMode.SELF) {
                    CommandUser self = commandContext.getSender();
                    user = this.userPipeline.get(self.uuid());
                } else {
                    user = commandContext.get("target");
                }

                if (!user.owns(this.permission, tag)) {
                    return ArgumentParseResult.failure(new TagArgumentException(input));
                }
            }

            inputQueue.remove();
            return ArgumentParseResult.success(tag);
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<@NonNull CommandUser> commandContext,
                final @NonNull String input
        ) {
            if (this.mode == TagParserMode.ANY || this.mode == TagParserMode.NON_SECRET) {
                Collection<Tag> tags = new ArrayList<>(this.tagsService.allTags());

                if (this.mode == TagParserMode.NON_SECRET) {
                    CommandUser self = commandContext.getSender();
                    TagsUser user = this.userPipeline.get(self.uuid());

                    tags.removeIf(tag -> !(user.owns(this.permission, tag) || tag.secret()));
                }

                return Lists.map(this.tagsService.allTags(), Tag::name);
            }

            final TagsUser target;

            if (this.mode == TagParserMode.SELF) {
                CommandUser self = commandContext.getSender();
                target = this.userPipeline.get(self.uuid());
            } else {
                target = commandContext.get("target");
            }

            return Lists.map(this.tagsService.allTags(target), Tag::name);
        }

    }

    public static final class TagArgumentException extends IllegalArgumentException {

        private final String input;

        private TagArgumentException(
                final @NonNull String input
        ) {
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }

    }

}
