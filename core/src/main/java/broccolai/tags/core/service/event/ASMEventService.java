package broccolai.tags.core.service.event;

import broccolai.tags.api.events.Event;
import broccolai.tags.api.events.EventListener;
import broccolai.tags.api.service.EventService;
import com.google.inject.Singleton;
import net.kyori.event.SimpleEventBus;
import net.kyori.event.method.MethodHandleEventExecutorFactory;
import net.kyori.event.method.MethodSubscriptionAdapter;
import net.kyori.event.method.SimpleMethodSubscriptionAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class ASMEventService extends SimpleEventBus<Event> implements EventService {
    private final MethodSubscriptionAdapter<EventListener> methodAdapter = new SimpleMethodSubscriptionAdapter<>(
            this,
            new MethodHandleEventExecutorFactory<>()
    );

    public ASMEventService() {
        super(Event.class);
    }

    @Override
    public void registerListeners(final @NonNull EventListener... events) {
        for (final EventListener listener : events) {
            this.methodAdapter.register(listener);
        }
    }
}
