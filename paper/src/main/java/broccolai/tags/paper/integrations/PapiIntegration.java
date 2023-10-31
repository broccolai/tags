package broccolai.tags.paper.integrations;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import com.google.inject.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PapiIntegration extends PlaceholderExpansion {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .build();

    private final @NonNull UserService userService;
    private final @NonNull TagsService tagsService;

    @Inject
    public PapiIntegration(
            final @NonNull UserService userService,
            final @NonNull TagsService tagsService
    ) {
        this.userService = userService;
        this.tagsService = tagsService;
    }

    @Override
    public String getIdentifier() {
        return "tags";
    }

    @Override
    public String getAuthor() {
        return "broccolai";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(final @NonNull OfflinePlayer player, final @NonNull String identifier) {
        TagsUser user = this.userService.get(player.getUniqueId());

        if ("current".equalsIgnoreCase(identifier)) {
            ConstructedTag tag = this.tagsService.load(user);
            return LEGACY.serialize(tag.component());
        }

        return "";
    }

}
