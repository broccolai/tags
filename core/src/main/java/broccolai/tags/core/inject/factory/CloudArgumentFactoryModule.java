package broccolai.tags.core.inject.factory;

import broccolai.tags.core.factory.CloudArgumentFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class CloudArgumentFactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new FactoryModuleBuilder()
                .build(CloudArgumentFactory.class));
    }

}
