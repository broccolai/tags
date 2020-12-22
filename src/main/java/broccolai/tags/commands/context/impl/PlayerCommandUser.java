package broccolai.tags.commands.context.impl;

import broccolai.tags.commands.context.CommandUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class PlayerCommandUser extends CommandUser.AbstractCommandUser {

    private final Player player;

    public PlayerCommandUser(final Player player, final Audience audience) {
        super(player, audience);
        this.player = player;
    }

    @Override
    public Player asSender() {
        return this.player;
    }

    @Override
    public UUID uniqueId() {
        return this.player.getUniqueId();
    }

}
