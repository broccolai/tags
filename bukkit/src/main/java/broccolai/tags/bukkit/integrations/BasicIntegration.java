package broccolai.tags.bukkit.integrations;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class BasicIntegration implements Listener {

    private final UserService userService;
    private final TagsService tagsService;

    @Inject
    public BasicIntegration(final @NonNull UserService userService, final @NonNull TagsService tagsService) {
        this.userService = userService;
        this.tagsService = tagsService;
    }

    @EventHandler
    public void onChat(final @NonNull AsyncChatEvent event) {
        event.renderer(((source, name, message, viewer) -> {
            ConstructedTag tag = this.tagsService.load(this.userService.get(source.getUniqueId()));
            return message.replaceText(builder -> builder.matchLiteral("%tag%").replacement(tag.component()));
        }));
    }

}
