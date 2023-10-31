package broccolai.tags.paper.commands.context;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperPlayerCommandUser extends PaperCommandUser {

    private final Player player;

    public PaperPlayerCommandUser(
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
