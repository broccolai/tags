package broccolai.tags.bukkit.commands.context;

import broccolai.tags.core.commands.context.CommandUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BukkitCommandUser implements CommandUser {

    private final CommandSender source;
    private final Audience audience;

    public BukkitCommandUser(final @NonNull CommandSender source, final @NonNull Audience audience) {
        this.source = source;
        this.audience = audience;
    }

    public final @NonNull CommandSender sender() {
        return this.source;
    }

    @Override
    public final @NonNull Audience audience() {
        return this.audience;
    }

}
