package broccolai.tags.core.service.data;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.model.user.impl.PlayerTagsUser;
import broccolai.tags.api.service.DataService;
import java.util.Optional;
import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.MetaNode;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class LPDataService implements DataService {

    private static final String CURRENT_TAG_NODE = "current-tag";

    private final LuckPerms luckPerms = LuckPermsProvider.get();

    @Override
    public @NonNull Optional<@NonNull TagsUser> loadUser(
            final @NonNull UUID uniqueId
    ) {
        User luckpermsUser = this.luckPerms.getUserManager()
                .getUser(uniqueId);

        if (luckpermsUser == null) {
            return Optional.empty();
        }

        String metaValue = luckpermsUser.getCachedData()
                .getMetaData()
                .getMetaValue(CURRENT_TAG_NODE);

        if (metaValue == null) {
            return Optional.empty();
        }

        return Optional.of(
                new PlayerTagsUser(uniqueId, Integer.parseInt(metaValue))
        );
    }

    @Override
    public void saveUser(final @NonNull TagsUser user) {
        Optional<Integer> potentialId = user.current();

        if (potentialId.isEmpty()) {
            return;
        }

        User luckpermsUser = this.luckPerms.getUserManager()
                .getUser(user.uuid());

        if (luckpermsUser == null) {
            return;
        }

        MetaNode metaNode = MetaNode.builder(CURRENT_TAG_NODE, String.valueOf(potentialId.get())).build();

        luckpermsUser.data().clear(node -> node.getKey().contains(CURRENT_TAG_NODE));
        luckpermsUser.data().add(metaNode);
        this.luckPerms.getUserManager().saveUser(luckpermsUser);
    }

}
