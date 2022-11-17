package broccolai.tags.core.inject;

import broccolai.tags.api.service.DataService;
import broccolai.tags.api.service.EventService;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.data.StorageMethod;
import broccolai.tags.core.service.data.LPDataService;
import broccolai.tags.core.service.data.H2DataService;
import broccolai.tags.core.service.event.ASMEventService;
import broccolai.tags.core.service.message.MiniMessageService;
import broccolai.tags.core.service.tags.MappedTagsService;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(MessageService.class).to(MiniMessageService.class);
        this.bind(TagsService.class).to(MappedTagsService.class);
        this.bind(EventService.class).to(ASMEventService.class);
    }

    @Provides
    @Singleton
    public DataService providesDataService(
            final @NonNull Injector injector,
            final @NonNull MainConfiguration mainConfiguration
    ) {
        boolean luckExists;
        try {
            Class.forName("net.luckperms.api.LuckPerms");
            luckExists = true;
        } catch (ClassNotFoundException e) {
            luckExists = false;
        }

        if (luckExists && mainConfiguration.storage.storageMethod == StorageMethod.LUCKPERMS) {
            return injector.getInstance(LPDataService.class);
        }

        Flyway.configure(this.getClass().getClassLoader())
                .baselineOnMigrate(true)
                .locations("classpath:queries/migrations")
                .dataSource(injector.getInstance(HikariDataSource.class))
                .load()
                .migrate();

        return injector.getInstance(H2DataService.class);
    }

}
