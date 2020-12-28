package broccolai.tags.integrations;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import com.google.inject.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

//todo: https://github.com/Hexaoxide/Carbon/issues/133
public final class TagsPlaceholders extends PlaceholderExpansion {

    private static final @NonNull MiniMessage MINI = MiniMessage.get();

    private final @NonNull UserPipeline userPipeline;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsPlaceholders(final @NonNull UserPipeline userPipeline, final @NonNull TagsService tagsService) {
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
            return user.current()
                    .map(this.tagsService::load)
                    .map(Tag::component)
                    .map(MINI::serialize)
                    .orElse("");
        }

        return "";
    }

}
