package broccolai.tags.bukkit.listeners;

import broccolai.tags.api.events.event.UserLoginEvent;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.EventService;
import broccolai.tags.api.service.TaskService;
import broccolai.tags.api.service.UserService;
import com.google.inject.Inject;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerListener implements Listener {

    private final @NonNull TaskService taskService;
    private final @NonNull UserService userService;
    private final @NonNull EventService eventService;

    @Inject
    public PlayerListener(
            final @NonNull TaskService taskService,
            final @NonNull UserService userService,
            final @NonNull EventService eventService
    ) {
        this.taskService = taskService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @EventHandler
    public void onPlayerLogin(final @NonNull PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        this.taskService.async(() -> {
            TagsUser user = this.userService.get(uuid);
            this.eventService.post(new UserLoginEvent(user));
        });
    }
}
