package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService extends Service {

    @NonNull TagsUser get(@NonNull UUID uuid);

    @NonNull TagsUser get(@NonNull String username);

    @NonNull Map<@NonNull UUID, @NonNull TagsUser> get(@NonNull Collection<UUID> uuids);

    @NonNull String name(@NonNull TagsUser user);

    @NonNull List<@NonNull String> onlineNames();

}
