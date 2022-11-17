package broccolai.tags.api.model.user.impl;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class ConsoleTagsUser implements TagsUser {

    public static final @NonNull UUID UUID = new UUID(0, 0);

    private @Nullable Integer currentTag;

    @Override
    public @NonNull UUID uuid() {
        return UUID;
    }

    @Override
    public void setCurrent(final @Nullable ConstructedTag tag) {
        this.currentTag = tag != null ? tag.id() : null;
    }

    @Override
    public @NonNull Optional<@NonNull Integer> current() {
        return Optional.ofNullable(this.currentTag);
    }

}
