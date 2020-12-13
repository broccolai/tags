package broccolai.tags.service.user.impl;

import broccolai.tags.model.TagsUser;
import broccolai.tags.model.impl.PlayerTagsUser;
import broccolai.tags.service.user.UserService;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserCreateService implements UserService {

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (UUID request : requests) {
            results.put(request, new PlayerTagsUser(request));
        }

        return results;
    }

}
