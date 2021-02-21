package broccolai.tags.core.commands;

import broccolai.tags.core.commands.context.CommandUser;
import cloud.commandframework.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PluginCommand {

    void register(@NonNull CommandManager<@NonNull CommandUser> commandManager);

}
