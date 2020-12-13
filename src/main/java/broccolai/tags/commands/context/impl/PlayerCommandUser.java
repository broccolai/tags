package broccolai.tags.commands.context.impl;

import broccolai.tags.commands.context.CommandUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public final class PlayerCommandUser extends CommandUser.AbstractCommandUser {

    private final Player player;

    public PlayerCommandUser(final Player player, final Audience audience) {
        super(player, audience);
        this.player = player;
    }

    @Override
    public boolean isAuthorized(final String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public Player asSender() {
        return this.player;
    }

}
