package broccolai.tags.inject;

import broccolai.tags.service.user.UserPipeline;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class UserModule extends AbstractModule {

    @Provides
    @Singleton
    UserPipeline providesUserPipeline(final @NonNull Injector injector) {
        return new UserPipeline(injector);
    }

}
