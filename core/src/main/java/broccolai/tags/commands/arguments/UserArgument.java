package broccolai.tags.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public class UserArgument extends CommandArgument<@NonNull CommandUser, @NonNull TagsUser> {

    @AssistedInject
    public UserArgument(
            final @NonNull UserPipeline userPipeline,
            final @Assisted("name") @NonNull String name,
            final @Assisted("required") boolean required
    ) {
        super(required, name, new UserParser(userPipeline), TagsUser.class);
    }

    public static final class UserParser implements ArgumentParser<@NonNull CommandUser, TagsUser> {

        private final @NonNull UserPipeline userPipeline;

        public UserParser(final @NonNull UserPipeline userPipeline) {
            this.userPipeline = userPipeline;
        }

        @Override
        @SuppressWarnings("deprecation")
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

            //todo: UUID Pipeline?
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
            TagsUser user = this.userPipeline.get(offlinePlayer.getUniqueId());

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
            return Lists.map(Bukkit.getOnlinePlayers(), Player::getName);
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
