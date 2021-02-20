package broccolai.tags.commands.context;

import broccolai.tags.commands.context.impl.ConsoleCommandUser;
import broccolai.tags.commands.context.impl.PlayerCommandUser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface CommandUser extends ForwardingAudience.Single {

    CommandSender sender();

    UUID uuid();

    static CommandUser from(CommandSender sender, BukkitAudiences audiences) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;

            return new ConsoleCommandUser(console, audiences.console());
        } else if (sender instanceof Player) {
            Player player = (Player) sender;

            return new PlayerCommandUser(player, audiences.player(player));
        }

        return null;
    }

    abstract class AbstractCommandUser implements CommandUser {

        private final CommandSender base;
        private final Audience audience;

        protected AbstractCommandUser(final CommandSender base, final Audience audience) {
            this.base = base;
            this.audience = audience;
        }

        @Override
        public CommandSender sender() {
            return this.base;
        }

        @Override
        public @NonNull Audience audience() {
            return this.audience;
        }

    }

}
