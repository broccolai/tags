package broccolai.tags.bukkit.commands.context;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class BukkitPlayerCommandUser extends BukkitCommandUser {

    private final Player player;

    public BukkitPlayerCommandUser(
            final @NonNull Player player,
            final @NonNull Audience audience
    ) {
        super(player, audience);
        this.player = player;
    }

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }

}
