package broccolai.tags.service.user.impl;

import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.data.DataService;
import broccolai.tags.service.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public final class UserSQLService implements UserService {

    private final @NonNull DataService dataService;

    @Inject
    public UserSQLService(final @NonNull DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (final UUID uuid : requests) {
            this.dataService.getUser(uuid).ifPresent(tagsUser -> results.put(uuid, tagsUser));
        }

        return results;
    }

}
