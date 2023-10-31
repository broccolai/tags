package broccolai.tags.api.events.event;

import broccolai.tags.api.events.Event;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import net.kyori.event.Cancellable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

public final class TagChangeEvent extends Cancellable.Impl implements Event {

    private final @NonNull TagsUser user;
    private final @NonNull ConstructedTag tag;

    public TagChangeEvent(final @NonNull TagsUser user, final @NonNull ConstructedTag tag) {
        this.user = user;
        this.tag = tag;
    }

    @Pure
    public @NonNull TagsUser user() {
        return this.user;
    }

    @Pure
    public @NonNull ConstructedTag tag() {
        return this.tag;
    }

}
