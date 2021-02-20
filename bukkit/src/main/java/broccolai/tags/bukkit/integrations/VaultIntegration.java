package broccolai.tags.bukkit.integrations;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VaultIntegration implements Listener {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .build();

    private final Plugin plugin;
    private final Chat chat;
    private final UserService userService;
    private final TagsService tagsService;

    @Inject
    public VaultIntegration(
            final @NonNull Plugin plugin,
            final @NonNull Chat chat,
            final @NonNull UserService userService,
            final @NonNull TagsService tagsService
    ) {
        this.plugin = plugin;
        this.chat = chat;
        this.userService = userService;
        this.tagsService = tagsService;
    }

    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            TagsUser user = this.userService.get(player.getUniqueId());
            Tag tag = this.tagsService.load(user);

            this.chat.setPlayerPrefix(player, LEGACY.serialize(tag.component()));
        });
    }
}
