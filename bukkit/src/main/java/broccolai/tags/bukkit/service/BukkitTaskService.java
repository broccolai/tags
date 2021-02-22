package broccolai.tags.bukkit.service;

import broccolai.tags.api.service.TaskService;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitTaskService implements TaskService {

    private final @NonNull Plugin plugin;

    @Inject
    public BukkitTaskService(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sync(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTask(this.plugin, runnable);
    }

    @Override
    public void async(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

}
