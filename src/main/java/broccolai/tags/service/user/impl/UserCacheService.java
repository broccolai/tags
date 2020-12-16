package broccolai.tags.service.user.impl;

import broccolai.tags.model.user.TagsUser;
import broccolai.tags.model.user.impl.ConsoleTagsUser;
import broccolai.tags.service.data.DataService;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Singleton
public final class UserCacheService implements UserService, Consumer<Map<UUID, TagsUser>>, Closeable {

    private final DataService dataService;

    private final Cache<UUID, TagsUser> uuidCache;
    private final TagsUser consoleUser;

    @Inject
    public UserCacheService(final DataService dataService, final TagsService service) {
        this.dataService = dataService;
        this.consoleUser = new ConsoleTagsUser(service);
        this.uuidCache = Caffeine.newBuilder()
                .maximumSize(100)
                .<UUID, TagsUser>removalListener((uuid, user, cause) -> this.dataService.saveUser(user))
                .build();
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

    @Override
    public void accept(final Map<UUID, TagsUser> uuidTagsUserMap) {
        this.uuidCache.putAll(uuidTagsUserMap);
    }

    @Override
    public void close() {
        this.uuidCache.invalidateAll();
    }

}
