package broccolai.tags.core.service.user.partials;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.DataService;
import broccolai.tags.core.service.user.PartialUserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public final class UserSQLService implements PartialUserService {

    private final @NonNull DataService dataService;

    @Inject
    public UserSQLService(final @NonNull DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (final UUID uuid : requests) {
            this.dataService.loadUser(uuid).ifPresent(tagsUser -> results.put(uuid, tagsUser));
        }

        return results;
    }

}
