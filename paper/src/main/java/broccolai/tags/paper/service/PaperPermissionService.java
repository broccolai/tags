package broccolai.tags.paper.service;

import broccolai.tags.api.model.Permissible;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.PermissionService;
import com.google.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperPermissionService implements PermissionService {

    private final @NonNull Permission permission;

    @Inject
    public PaperPermissionService(final @NonNull Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean has(final TagsUser user, final Permissible permissible) {
        return this.permission.playerHas(null, this.player(user), permissible.permission());
    }

    @Override
    public void grant(final TagsUser user, final Permissible permissible) {
        this.permission.playerAdd(null, this.player(user), permissible.permission());
    }

    @Override
    public void remove(final TagsUser user, final Permissible permissible) {
        this.permission.playerRemove(null, this.player(user), permissible.permission());
    }

    private Player player(final @NonNull TagsUser user) {
        return Bukkit.getPlayer(user.uuid());
    }

}
