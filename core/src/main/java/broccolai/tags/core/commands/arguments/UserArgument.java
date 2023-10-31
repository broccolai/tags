package broccolai.tags.core.commands.arguments;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.commands.context.CommandUser;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import java.util.List;
import java.util.Queue;
import org.checkerframework.checker.nullness.qual.NonNull;

public class UserArgument extends CommandArgument<@NonNull CommandUser, @NonNull TagsUser> {

    @AssistedInject
    public UserArgument(
            final @NonNull UserService userService,
            final @Assisted("name") @NonNull String name,
            final @Assisted("required") boolean required
    ) {
        super(required, name, new UserParser(userService), TagsUser.class);
    }

    public static final class UserParser implements ArgumentParser<@NonNull CommandUser, TagsUser> {

        private final @NonNull UserService userService;

        public UserParser(final @NonNull UserService userService) {
            this.userService = userService;
        }

        @Override
        public @NonNull ArgumentParseResult<TagsUser> parse(
                final @NonNull CommandContext<@NonNull CommandUser> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            if (commandContext.isSuggestions()) {
                return ArgumentParseResult.success(TagsUser.CONSOLE);
            }

            final String input = inputQueue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(UserArgument.class, commandContext));
            }

            TagsUser user = this.userService.get(input);

            if (user == null) {
                return ArgumentParseResult.failure(new UserArgumentException(input));
            }

            inputQueue.remove();
            return ArgumentParseResult.success(user);
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<@NonNull CommandUser> commandContext,
                final @NonNull String input
        ) {
            return this.userService.onlineNames();
        }

    }

    public static final class UserArgumentException extends IllegalArgumentException {

        private static final long serialVersionUID = 7407374822368222827L;

        private final String input;

        private UserArgumentException(
                final @NonNull String input
        ) {
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }

    }

}
