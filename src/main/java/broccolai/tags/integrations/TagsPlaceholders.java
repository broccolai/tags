package broccolai.tags.integrations;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import com.google.inject.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagsPlaceholders extends PlaceholderExpansion {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .build();

    private final @NonNull UserPipeline userPipeline;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsPlaceholders(
            final @NonNull UserPipeline userPipeline,
            final @NonNull TagsService tagsService
    ) {
        this.userPipeline = userPipeline;
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
        TagsUser user = this.userPipeline.get(player.getUniqueId());

        if (identifier.equalsIgnoreCase("current")) {
            Tag tag = this.tagsService.load(user);
            return LEGACY.serialize(tag.component());
        }

        return "";
    }

}
