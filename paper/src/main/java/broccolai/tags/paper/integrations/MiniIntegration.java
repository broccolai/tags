package broccolai.tags.paper.integrations;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.api.service.UserService;
import com.google.inject.Inject;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MiniIntegration {

    private final UserService userService;
    private final TagsService tagsService;

    @Inject
    public MiniIntegration(final @NonNull UserService userService, final @NonNull TagsService tagsService) {
        this.userService = userService;
        this.tagsService = tagsService;
    }

    public void registerExpansion() {
        Expansion.builder("tags")
            .audiencePlaceholder("current", this::placeholderFromContext)
            .build()
            .register();
    }

    private @Nullable Tag placeholderFromContext(Audience audience, ArgumentQueue queue, Context context) {
        return audience
            .get(Identity.UUID)
            .map(this.userService::get)
            .map(this.tagsService::load)
            .map(ConstructedTag::component)
            .map(Tag::selfClosingInserting)
            .orElse(null);
    }

}
