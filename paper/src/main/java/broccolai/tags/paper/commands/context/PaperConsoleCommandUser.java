package broccolai.tags.paper.commands.context;

import broccolai.tags.api.model.user.impl.ConsoleTagsUser;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperConsoleCommandUser extends PaperCommandUser {

    public PaperConsoleCommandUser(final @NonNull CommandSender source) {
        super(source);
    }

    @Override
    public UUID uuid() {
        return ConsoleTagsUser.UUID;
    }

}
