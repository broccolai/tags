package broccolai.tags.model.user.impl;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class ConsoleTagsUser implements TagsUser {

    public static final @NonNull UUID UUID = new UUID(0, 0);

    private Integer currentTag;

    @Override
    public @NonNull UUID uuid() {
        return UUID;
    }

    @Override
    public void setCurrent(final @Nullable Tag tag) {
        this.currentTag = tag != null ? tag.id() : null;
    }

    @Override
    public void grant(final @NonNull Permission permission, final @NonNull Tag tag) {
        // unnecessary
    }

    @Override
    public void remove(final @NonNull Permission permission, final @NonNull Tag tag) {
        // unnecessary
    }

    @Override
    public boolean owns(final @NonNull Permission permission, final @NonNull Tag tag) {
        return true;
    }

    @Override
    public boolean hasPermission(
            final @NonNull Permission permission,
            final @NonNull String perm
    ) {
        return true;
    }

    @Override
    public @NonNull Optional<Integer> current() {
        return Optional.ofNullable(this.currentTag);
    }

}
