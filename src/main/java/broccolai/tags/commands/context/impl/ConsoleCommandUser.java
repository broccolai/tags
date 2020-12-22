package broccolai.tags.commands.context.impl;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.model.user.impl.ConsoleTagsUser;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

public final class ConsoleCommandUser extends CommandUser.AbstractCommandUser {

    private final ConsoleCommandSender sender;

    public ConsoleCommandUser(final ConsoleCommandSender sender, final Audience audience) {
        super(sender, audience);
        this.sender = sender;
    }

    @Override
    public ConsoleCommandSender asSender() {
        return this.sender;
    }

    @Override
    public UUID uniqueId() {
        return ConsoleTagsUser.UUID;
    }

}
