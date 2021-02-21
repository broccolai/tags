package broccolai.tags.core.inject;

import broccolai.tags.api.service.DataService;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.core.service.data.SQLDataService;
import broccolai.tags.core.service.message.MiniMessageService;
import broccolai.tags.core.service.tags.MappedTagsService;
import com.google.inject.AbstractModule;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(DataService.class).to(SQLDataService.class);
        this.bind(MessageService.class).to(MiniMessageService.class);
        this.bind(TagsService.class).to(MappedTagsService.class);
    }

}
