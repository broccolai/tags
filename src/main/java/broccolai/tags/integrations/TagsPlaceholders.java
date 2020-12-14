package broccolai.tags.integrations;

import broccolai.tags.model.TagsUser;
import broccolai.tags.service.user.UserPipeline;
import com.google.inject.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagsPlaceholders extends PlaceholderExpansion {

    private final UserPipeline userPipeline;

    @Inject
    public TagsPlaceholders(final UserPipeline userPipeline) {
        this.userPipeline = userPipeline;
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
    public String onRequest(final OfflinePlayer player, final @NonNull String identifier) {
        TagsUser user = this.userPipeline.get(player.getUniqueId());

        if (identifier.equalsIgnoreCase("current")) {
            return user.getUuid().toString();
        }

        return "";
    }

}
