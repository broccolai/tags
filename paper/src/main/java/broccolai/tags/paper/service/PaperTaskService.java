package broccolai.tags.paper.service;

import broccolai.tags.api.service.TaskService;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperTaskService implements TaskService {

    private final @NonNull Plugin plugin;

    @Inject
    public PaperTaskService(final @NonNull Plugin plugin) {
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
