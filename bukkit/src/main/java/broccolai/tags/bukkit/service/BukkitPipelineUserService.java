package broccolai.tags.bukkit.service;

import broccolai.corn.core.Lists;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.core.service.user.PipelineUserService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitPipelineUserService extends PipelineUserService {

    @Inject
    public BukkitPipelineUserService(final @NonNull Injector injector) {
        super(injector);
    }

    @Override
    public @NonNull TagsUser get(final @NonNull String username) {
        return this.get(Bukkit.getOfflinePlayer(username).getUniqueId());
    }

    @Override
    public @NonNull String name(final @NonNull TagsUser user) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(user.uuid());
        return Objects.requireNonNull(player.getName());
    }

    @Override
    public @NonNull List<@NonNull String> onlineNames() {
        return Lists.map(Bukkit.getOnlinePlayers(), Player::getName);
    }

}
