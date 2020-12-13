package broccolai.tags.model.impl;

import broccolai.tags.model.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class ConsoleTagsUser implements TagsUser {

    public static final @NonNull UUID UUID = new UUID(0, 0);

    @Override
    public @NonNull UUID getUuid() {
        return UUID;
    }

}
