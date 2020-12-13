package broccolai.tags.commands.context.impl;

import broccolai.tags.commands.context.CommandUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.ConsoleCommandSender;

public final class ConsoleCommandUser extends CommandUser.AbstractCommandUser {

    private final ConsoleCommandSender sender;

    public ConsoleCommandUser(final ConsoleCommandSender sender, final Audience audience) {
        super(sender, audience);
        this.sender = sender;
    }

    @Override
    public boolean isAuthorized(final String permission) {
        return true;
    }

    @Override
    public ConsoleCommandSender asSender() {
        return this.sender;
    }

}
