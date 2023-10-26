package broccolai.tags.core.service.user.partials;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.model.user.impl.PlayerTagsUser;
import broccolai.tags.core.service.user.PartialUserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class UserCreateService implements PartialUserService {

    @Override
    public @NonNull Map<UUID, TagsUser> handleRequests(final @NonNull List<UUID> requests) {
        Map<UUID, TagsUser> results = new HashMap<>();

        for (UUID request : requests) {
            results.put(request, new PlayerTagsUser(request, null));
        }

        return results;
    }

}
