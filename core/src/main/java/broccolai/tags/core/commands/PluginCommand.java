package broccolai.tags.core.commands;

import broccolai.tags.core.commands.context.CommandUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;

public interface PluginCommand {

    void register(@NonNull CommandManager<@NonNull CommandUser> commandManager);

}
