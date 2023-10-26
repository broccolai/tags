package broccolai.tags.bukkit.commands.context;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitPlayerCommandUser extends BukkitCommandUser {

    private final Player player;

    public BukkitPlayerCommandUser(
            final @NonNull Player player
    ) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }

    public @NonNull Player player() {
        return this.player;
    }

}
