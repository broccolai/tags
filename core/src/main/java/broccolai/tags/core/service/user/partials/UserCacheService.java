package broccolai.tags.core.service.user.partials;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.model.user.impl.ConsoleTagsUser;
import broccolai.tags.api.service.DataService;
import broccolai.tags.core.service.user.PartialUserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class UserCacheService implements PartialUserService, Consumer<Map<UUID, TagsUser>>, Closeable {

    private final @NonNull DataService dataService;

    private final @NonNull Cache<UUID, TagsUser> uuidCache;

    @Inject
    public UserCacheService(final @NonNull DataService dataService) {
        this.dataService = dataService;
        this.uuidCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .<UUID, TagsUser>removalListener(notification -> this.dataService.saveUser(notification.getValue()))
                .build();
    }

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (final UUID request : requests) {
            if (request.equals(ConsoleTagsUser.UUID)) {
                results.put(request, TagsUser.CONSOLE);
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
    public void accept(final @NonNull Map<UUID, TagsUser> uuidTagsUserMap) {
        this.uuidCache.putAll(uuidTagsUserMap);
    }

    @Override
    public void close() {
        this.uuidCache.asMap().values().forEach(this.dataService::saveUser);
    }

}
