package broccolai.tags.core.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import broccolai.tags.core.commands.context.CommandUser;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public final class TagParser implements ArgumentParser<@NonNull CommandUser, ConstructedTag>,
    BlockingSuggestionProvider.Strings<CommandUser>,
    ParserDescriptor<CommandUser, ConstructedTag> {

    private final @NonNull PermissionService permissionService;
    private final @NonNull TagsService tagsService;
    private final @NonNull UserService userService;
    private final @NonNull TagParserMode mode;

    @AssistedInject
    public TagParser(
        final @NonNull PermissionService permissionService,
        final @NonNull TagsService tagsService,
        final @NonNull UserService userService,
        final @Assisted("mode") @NonNull TagParserMode mode
    ) {
        this.tagsService = tagsService;
        this.userService = userService;
        this.permissionService = permissionService;
        this.mode = mode;
    }

    @Override
    public @NonNull ArgumentParseResult<ConstructedTag> parse(
        final @NonNull CommandContext<@NonNull CommandUser> commandContext,
        final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();

        ConstructedTag tag = this.tagsService.load(input);

        if (tag == null) {
            return ArgumentParseResult.failure(new TagParseException(input));
        }

        if (this.mode == TagParserMode.NON_SECRET && tag.secret()) {
            CommandUser self = commandContext.sender();
            TagsUser user = this.userService.get(self.uuid());

            if (!this.permissionService.has(user, tag)) {
                return ArgumentParseResult.failure(new TagParseException(input));
            }
        } else if (this.mode == TagParserMode.SELF || this.mode == TagParserMode.TARGET) {
            final TagsUser user;

            if (this.mode == TagParserMode.SELF) {
                CommandUser self = commandContext.sender();
                user = this.userService.get(self.uuid());
            } else {
                user = commandContext.get("target");
            }

            if (!this.permissionService.has(user, tag)) {
                return ArgumentParseResult.failure(new TagParseException(input));
            }
        }

        return ArgumentParseResult.success(tag);
    }

    @Override
    public @NonNull List<String> stringSuggestions(
        final @NonNull CommandContext<@NonNull CommandUser> commandContext,
        final @NonNull CommandInput commandInput
    ) {
        if (this.mode == TagParserMode.ANY || this.mode == TagParserMode.NON_SECRET) {
            Collection<ConstructedTag> tags = new ArrayList<>(this.tagsService.allTags());

            if (this.mode == TagParserMode.NON_SECRET) {
                CommandUser self = commandContext.sender();
                TagsUser user = this.userService.get(self.uuid());

                tags.removeIf(tag -> !(this.permissionService.has(user, tag) || tag.secret()));
            }

            return Lists.map(this.tagsService.allTags(), ConstructedTag::name);
        }

        final TagsUser target;

        if (this.mode == TagParserMode.SELF) {
            CommandUser self = commandContext.sender();
            target = this.userService.get(self.uuid());
        } else {
            target = commandContext.get("target");
        }

        return Lists.map(this.tagsService.allTags(target), ConstructedTag::name);
    }

    @Override
    public @NonNull ArgumentParser<CommandUser, ConstructedTag> parser() {
        return this;
    }

    @Override
    public @NonNull TypeToken<ConstructedTag> valueType() {
        return TypeToken.get(ConstructedTag.class);
    }

    public static final class TagParseException extends IllegalArgumentException {

        private static final long serialVersionUID = 926974199863043883L;

        private final String input;

        private TagParseException(
            final @NonNull String input
        ) {
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }

    }

}
