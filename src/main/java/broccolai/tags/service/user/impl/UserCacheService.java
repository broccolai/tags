package broccolai.tags.service.user.impl;

import broccolai.tags.model.TagsUser;
import broccolai.tags.model.impl.ConsoleTagsUser;
import broccolai.tags.service.user.UserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserCacheService implements UserService {

    private final Cache<UUID, TagsUser> uuidCache;

    public UserCacheService() {
        this.uuidCache = CacheBuilder.newBuilder().maximumSize(100).build();
    }

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (final UUID request : requests) {
            if (request.equals(ConsoleTagsUser.UUID)) {
                results.put(request, TagsUser.CONSOLE);
                continue;
            }

            results.put(request, this.uuidCache.getIfPresent(request));
        }

        return results;
    }

}
