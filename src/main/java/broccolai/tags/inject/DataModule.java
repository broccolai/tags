package broccolai.tags.inject;

import broccolai.tags.service.data.DataService;
import broccolai.tags.service.data.impl.SQLDataService;
import com.google.inject.AbstractModule;

public final class DataModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(DataService.class).to(SQLDataService.class);
    }

}
