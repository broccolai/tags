package broccolai.tags.api.service;

import broccolai.tags.api.events.Event;
import broccolai.tags.api.events.EventListener;
import broccolai.tags.api.model.Service;
import net.kyori.event.EventBus;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface EventService extends EventBus<Event>, Service {

    void register(@NonNull EventListener listener);

}
