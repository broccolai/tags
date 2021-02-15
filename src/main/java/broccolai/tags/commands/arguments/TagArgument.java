package broccolai.tags.commands.arguments;

import broccolai.corn.core.Lists;
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
            final @Assisted("selfTarget") boolean selfTarget,
            final @Assisted("shouldCheck") boolean shouldCheck
    ) {
        super(true, name, new TagParser(tagsService, userPipeline, permission, selfTarget, shouldCheck), Tag.class);
    }

    public static final class TagParser implements ArgumentParser<@NonNull CommandUser, Tag> {

        private final @NonNull TagsService tagsService;
        private final @NonNull UserPipeline userPipeline;
        private final @NonNull Permission permission;
        private final boolean selfTarget;
        private final boolean shouldCheck;

        public TagParser(
                final @NonNull TagsService tagsService,
                final @NonNull UserPipeline userPipeline,
                final @NonNull Permission permission,
                final boolean selfTarget,
                final boolean shouldCheck
        ) {
            this.tagsService = tagsService;
            this.userPipeline = userPipeline;
            this.permission = permission;
            this.selfTarget = selfTarget;
            this.shouldCheck = shouldCheck;
        }

        @Override
        public @NonNull ArgumentParseResult<Tag> parse(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final Queue<String> inputQueue
        ) {
            final String input = inputQueue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(TagParser.class, commandContext));
            }

            Tag tag = this.tagsService.load(input);

            if (tag == null) {
                return ArgumentParseResult.failure(new TagArgumentException(input));
            }

            if (shouldCheck) {
                TagsUser user;

                if (this.selfTarget) {
                    CommandUser commandUser = commandContext.getSender();
                    user = this.userPipeline.get(commandUser.uuid());
                } else {
                    user = commandContext.get("target");
                }

                if (!user.owns(permission, tag)) {
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
            Collection<Tag> tags;

            if (this.shouldCheck) {
                final TagsUser target;

                if (this.selfTarget) {
                    CommandUser user = commandContext.getSender();
                    target = this.userPipeline.get(user.uuid());
                } else {
                    target = commandContext.get("target");
                }

                tags = this.tagsService.allTags(target);
            } else {
                tags = this.tagsService.allTags();
            }

            return Lists.map(tags, Tag::name);
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
