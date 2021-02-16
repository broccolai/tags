package broccolai.tags.integrations;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserPipeline;
import com.google.inject.Inject;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VaultIntegration {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .build();

    @Inject
    public VaultIntegration(
            final @NonNull Plugin plugin,
            final @NonNull Permission permission,
            final @NonNull UserPipeline userPipeline,
            final @NonNull TagsService tagsService
    ) {
        Chat provider = new ChatHandler(permission, userPipeline, tagsService);
        Bukkit.getServicesManager().register(Chat.class, provider, plugin, ServicePriority.High);
    }

    public static final class ChatHandler extends Chat {

        private final Permission permission;
        private final UserPipeline userPipeline;
        private final TagsService tagsService;

        public ChatHandler(
                final @NonNull Permission permission,
                final @NonNull UserPipeline userPipeline,
                final @NonNull TagsService tagsService
        ) {
            super(permission);
            this.permission = permission;
            this.userPipeline = userPipeline;
            this.tagsService = tagsService;
        }

        @Override
        public String getName() {
            return "Tags";
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getPlayerPrefix(final String world, final String player) {
            return this.getPlayerPrefix(world, Bukkit.getOfflinePlayer(player));
        }

        @Override
        public String getPlayerPrefix(final String world, final OfflinePlayer player) {
            TagsUser user = this.userPipeline.get(player.getUniqueId());
            return user.current()
                    .map(this.tagsService::load)
                    .filter(tag -> user.owns(this.permission, tag))
                    .map(Tag::component)
                    .map(LEGACY::serialize)
                    .orElse("");
        }

        @Override
        public void setPlayerPrefix(final String world, final String player, final String prefix) {

        }

        @Override
        public String getPlayerSuffix(final String world, final String player) {
            return "";
        }

        @Override
        public void setPlayerSuffix(final String world, final String player, final String suffix) {

        }

        @Override
        public String getGroupPrefix(final String world, final String group) {
            return "";
        }

        @Override
        public void setGroupPrefix(final String world, final String group, final String prefix) {

        }

        @Override
        public String getGroupSuffix(final String world, final String group) {
            return "";
        }

        @Override
        public void setGroupSuffix(final String world, final String group, final String suffix) {

        }

        @Override
        public int getPlayerInfoInteger(final String world, final String player, final String node, final int defaultValue) {
            return 0;
        }

        @Override
        public void setPlayerInfoInteger(final String world, final String player, final String node, final int value) {

        }

        @Override
        public int getGroupInfoInteger(final String world, final String group, final String node, final int defaultValue) {
            return 0;
        }

        @Override
        public void setGroupInfoInteger(final String world, final String group, final String node, final int value) {

        }

        @Override
        public double getPlayerInfoDouble(final String world, final String player, final String node, final double defaultValue) {
            return 0;
        }

        @Override
        public void setPlayerInfoDouble(final String world, final String player, final String node, final double value) {

        }

        @Override
        public double getGroupInfoDouble(final String world, final String group, final String node, final double defaultValue) {
            return 0;
        }

        @Override
        public void setGroupInfoDouble(final String world, final String group, final String node, final double value) {

        }

        @Override
        public boolean getPlayerInfoBoolean(
                final String world,
                final String player,
                final String node,
                final boolean defaultValue
        ) {
            return false;
        }

        @Override
        public void setPlayerInfoBoolean(final String world, final String player, final String node, final boolean value) {

        }

        @Override
        public boolean getGroupInfoBoolean(
                final String world,
                final String group,
                final String node,
                final boolean defaultValue
        ) {
            return false;
        }

        @Override
        public void setGroupInfoBoolean(final String world, final String group, final String node, final boolean value) {

        }

        @Override
        public String getPlayerInfoString(final String world, final String player, final String node, final String defaultValue) {
            return null;
        }

        @Override
        public void setPlayerInfoString(final String world, final String player, final String node, final String value) {

        }

        @Override
        public String getGroupInfoString(final String world, final String group, final String node, final String defaultValue) {
            return null;
        }

        @Override
        public void setGroupInfoString(final String world, final String group, final String node, final String value) {

        }

    }

}
