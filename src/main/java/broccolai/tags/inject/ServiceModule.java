package broccolai.tags.inject;

import broccolai.tags.service.data.DataService;
import broccolai.tags.service.data.impl.SQLDataService;
import broccolai.tags.service.message.MessageService;
import broccolai.tags.service.message.impl.MiniMessageService;
import com.google.inject.AbstractModule;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(DataService.class).to(SQLDataService.class);
        this.bind(MessageService.class).to(MiniMessageService.class);
    }

}
