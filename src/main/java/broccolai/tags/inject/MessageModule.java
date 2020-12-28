package broccolai.tags.inject;

import broccolai.tags.service.message.MessageService;
import broccolai.tags.service.message.impl.MiniMessageService;
import com.google.inject.AbstractModule;

public final class MessageModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(MessageService.class).to(MiniMessageService.class);
    }

}
