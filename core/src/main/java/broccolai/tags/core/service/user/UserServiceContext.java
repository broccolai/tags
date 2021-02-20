package broccolai.tags.core.service.user;

import broccolai.tags.api.model.user.TagsUser;
import cloud.commandframework.services.ChunkedRequestContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

final class UserServiceContext extends ChunkedRequestContext<UUID, TagsUser> {

    UserServiceContext(final @NonNull Collection<UUID> requests) {
        super(requests);
    }

}
