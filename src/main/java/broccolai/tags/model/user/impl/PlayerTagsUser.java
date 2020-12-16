package broccolai.tags.model.user.impl;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class PlayerTagsUser implements TagsUser {

    private final @NonNull UUID uuid;
    private final @NonNull Map<Integer, Tag> tags;
    private @Nullable Integer current;

    public PlayerTagsUser(
            final @NonNull UUID uuid,
            final @NonNull Map<Integer, Tag> tags,
            final @Nullable Integer current
    ) {
        this.uuid = uuid;
        this.tags = tags;
        this.current = current;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Override
    public void setCurrent(@Nullable final Tag tag) {
        this.current = tag != null ? tag.id() : null;
    }

    @Override
    public @NonNull Optional<Tag> current() {
        return Optional.ofNullable(this.tags.get(this.current));
    }

    @Override
    public @NonNull Collection<Tag> tags() {
        return Collections.unmodifiableCollection(tags.values());
    }

}
