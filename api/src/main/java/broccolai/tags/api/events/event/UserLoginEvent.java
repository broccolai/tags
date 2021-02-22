package broccolai.tags.api.events.event;

import broccolai.tags.api.events.Event;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class UserLoginEvent implements Event {

    private final @NonNull TagsUser user;

    public UserLoginEvent(final @NonNull TagsUser user) {
        this.user = user;
    }

    public @NonNull TagsUser user() {
        return this.user;
    }

}
