package broccolai.tags.paper.commands.context;

import broccolai.tags.core.commands.context.CommandUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class PaperCommandUser implements CommandUser {

    private final CommandSender source;

    public PaperCommandUser(final @NonNull CommandSender source) {
        this.source = source;
    }

    public final @NonNull CommandSender sender() {
        return this.source;
    }

    @Override
    public final @NonNull Audience audience() {
        return this.source;
    }

}
