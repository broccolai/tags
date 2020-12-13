package broccolai.tags.model.impl;

import broccolai.tags.model.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class PlayerTagsUser implements TagsUser {

    private final @NonNull UUID uuid;

    public PlayerTagsUser(
            final @NonNull UUID uuid
    ) {
        this.uuid = uuid;
    }

    @Override
    public @NonNull UUID getUuid() {
        return this.uuid;
    }

}
