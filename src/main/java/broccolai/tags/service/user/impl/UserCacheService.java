package broccolai.tags.service.user.impl;

import broccolai.tags.model.user.TagsUser;
import broccolai.tags.model.user.impl.ConsoleTagsUser;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserCacheService implements UserService {

    private final Cache<UUID, TagsUser> uuidCache;
    private final TagsUser consoleUser;

    @Inject
    public UserCacheService(final TagsService service) {
        this.consoleUser = new ConsoleTagsUser(service);
        this.uuidCache = CacheBuilder.newBuilder().maximumSize(100).build();
    }

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (final UUID request : requests) {
            if (request.equals(ConsoleTagsUser.UUID)) {
                results.put(request, this.consoleUser);
                continue;
            }

            TagsUser user = this.uuidCache.getIfPresent(request);

            if (user != null) {
                results.put(request, this.uuidCache.getIfPresent(request));
            }
        }

        return results;
    }

}
