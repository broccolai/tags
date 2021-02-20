package broccolai.tags.model.user.impl;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

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
    public void setCurrent(final @Nullable Tag tag) {
        this.current = tag != null ? tag.id() : null;
    }

    @Override
    public void grant(final @NonNull Permission permission, final @NonNull Tag tag) {
        permission.playerAdd(null, Bukkit.getOfflinePlayer(this.uuid), "tags.tag." + tag.id());
    }

    @Override
    public void remove(final @NonNull Permission permission, final @NonNull Tag tag) {
        permission.playerRemove(null, Bukkit.getOfflinePlayer(this.uuid), "tags.tag." + tag.id());
    }

    @Override
    public boolean owns(final @NonNull Permission permission, final @NonNull Tag tag) {
        return this.hasPermission(permission, "tags.tag." + tag.id());
    }

    @Override
    public boolean hasPermission(final @NonNull Permission permission, final @NonNull String perm) {
        return permission.playerHas(null, Bukkit.getOfflinePlayer(this.uuid), perm);
    }

    @Override
    public @NonNull Optional<Integer> current() {
        return Optional.ofNullable(this.current);
    }

}
