package broccolai.tags.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.user.UserPipeline;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
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
    public UserArgument(final @NonNull UserPipeline userPipeline, final @Assisted("name") @NonNull String name) {
        super(true, name, new UserParser(userPipeline), TagsUser.class);
    }

    public static final class UserParser implements ArgumentParser<@NonNull CommandUser, TagsUser> {

        private final @NonNull UserPipeline userPipeline;

        public UserParser(final @NonNull UserPipeline userPipeline) {
            this.userPipeline = userPipeline;
        }

        @Override
        public @NonNull ArgumentParseResult<TagsUser> parse(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final Queue<String> inputQueue
        ) {
            if (commandContext.isSuggestions()) {
                return ArgumentParseResult.success(TagsUser.CONSOLE);
            }

            final String input = inputQueue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NullPointerException("Expected tag name"));
            }

            //todo: UUID Pipeline?
            //noinspection deprecation
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
            TagsUser user = this.userPipeline.get(offlinePlayer.getUniqueId());

            if (user == null) {
                return ArgumentParseResult.failure(new NullPointerException("Could not find user with name " + input));
            }

            inputQueue.remove();
            return ArgumentParseResult.success(user);
        }

        @Override
        public @NonNull List<String> suggestions(
                @NonNull final CommandContext<@NonNull CommandUser> commandContext,
                @NonNull final String input
        ) {
            return Lists.map(Bukkit.getOnlinePlayers(), Player::getName);
        }

    }

}
