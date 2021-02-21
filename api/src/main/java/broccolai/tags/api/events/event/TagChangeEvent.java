package broccolai.tags.api.events.event;

import broccolai.tags.api.events.Event;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagChangeEvent implements Event {

    private final @NonNull TagsUser user;
    private final @NonNull Tag tag;

    public TagChangeEvent(final @NonNull TagsUser user, final @NonNull Tag tag) {
        this.user = user;
        this.tag = tag;
    }

    public @NonNull TagsUser user() {
        return this.user;
    }

    public @NonNull Tag tag() {
        return this.tag;
    }

}
