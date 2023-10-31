package broccolai.tags.api.model.user.impl;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PlayerTagsUser implements TagsUser {

    private final @NonNull UUID uuid;
    private @Nullable Integer current;

    public PlayerTagsUser(
            final @NonNull UUID uuid,
            final @Nullable Integer current
    ) {
        this.uuid = uuid;
        this.current = current;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Override
    public void current(final @Nullable ConstructedTag tag) {
        this.current = tag != null ? tag.id() : null;
    }

    @Override
    public @NonNull Optional<Integer> current() {
        return Optional.ofNullable(this.current);
    }

}
