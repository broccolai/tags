package broccolai.tags.core.service.action;

import broccolai.tags.api.events.event.TagChangeEvent;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.ActionService;
import broccolai.tags.api.service.EventService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class EventActionService implements ActionService {

    private final EventService eventService;

    @Inject
    public EventActionService(final @NonNull EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean select(final @NonNull TagsUser user, final @NonNull ConstructedTag tag) {
        TagChangeEvent event = new TagChangeEvent(user, tag);
        this.eventService.post(event);

        return !event.cancelled();
    }

    @Override
    public void remove(final @NonNull TagsUser user) {

    }

}
