package broccolai.tags.core.subscribers;

import broccolai.tags.api.events.EventListener;
import broccolai.tags.api.events.event.TagChangeEvent;
import net.kyori.event.PostOrders;
import net.kyori.event.method.annotation.PostOrder;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagChangeSubscriber implements EventListener {

    @Subscribe
    @PostOrder(PostOrders.LAST)
    public void onTagChange(final @NonNull TagChangeEvent event) {
        event.user().current(event.tag());
    }

}
