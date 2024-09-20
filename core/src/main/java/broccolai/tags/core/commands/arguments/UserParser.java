package broccolai.tags.core.commands.arguments;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.commands.context.CommandUser;
import io.leangen.geantyref.TypeToken;
import jakarta.inject.Inject;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public final class UserParser implements ArgumentParser<@NonNull CommandUser, TagsUser>,
    BlockingSuggestionProvider.Strings<CommandUser>,
    ParserDescriptor<CommandUser, TagsUser> {

    private final @NonNull UserService userService;

    @Inject
    public UserParser(final @NonNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull ArgumentParseResult<TagsUser> parse(
        final @NonNull CommandContext<@NonNull CommandUser> commandContext,
        final @NonNull CommandInput commandInput
    ) {
        if (commandContext.isSuggestions()) {
            return ArgumentParseResult.success(TagsUser.CONSOLE);
        }

        final String input = commandInput.readString();

        TagsUser user = this.userService.get(input);

        if (user == null) {
            return ArgumentParseResult.failure(new UserParseException(input));
        }

        return ArgumentParseResult.success(user);
    }

    @Override
    public @NonNull List<String> stringSuggestions(
        final @NonNull CommandContext<@NonNull CommandUser> commandContext,
        final @NonNull CommandInput input
    ) {
        return this.userService.onlineNames();
    }

    @Override
    public @NonNull ArgumentParser<CommandUser, TagsUser> parser() {
        return this;
    }

    @Override
    public @NonNull TypeToken<TagsUser> valueType() {
        return TypeToken.get(TagsUser.class);
    }

    public static final class UserParseException extends IllegalArgumentException {

        private static final long serialVersionUID = 7407374822368222827L;

        private final String input;

        private UserParseException(
            final @NonNull String input
        ) {
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }

    }

}
