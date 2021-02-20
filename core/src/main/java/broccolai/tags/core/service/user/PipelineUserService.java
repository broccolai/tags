package broccolai.tags.core.service.user;

import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.service.user.partials.UserCacheService;
import broccolai.tags.core.service.user.partials.UserCreateService;
import broccolai.tags.core.service.user.partials.UserSQLService;
import cloud.commandframework.services.ServicePipeline;
import com.google.inject.Injector;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public abstract class PipelineUserService implements UserService {

    private final @NonNull ServicePipeline pipeline = ServicePipeline.builder().build();
    private final @NonNull UserCacheService cacheService;

    public PipelineUserService(final @NonNull Injector injector) {
        this.cacheService = injector.getInstance(UserCacheService.class);

        this.pipeline.registerServiceType(TypeToken.get(PartialUserService.class), new UserCreateService())
                .registerServiceImplementation(
                        PartialUserService.class,
                        injector.getInstance(UserSQLService.class),
                        Collections.emptyList()
                )
                .registerServiceImplementation(PartialUserService.class, this.cacheService, Collections.emptyList());
    }

    @Override
    public final @NonNull TagsUser get(final @NonNull UUID uniqueId) {
        return this.get(Collections.singletonList(uniqueId)).get(uniqueId);
    }

    @Override
    public final @NonNull Map<@NonNull UUID, @NonNull TagsUser> get(final @NonNull Collection<UUID> uniqueIds) {
        Map<UUID, TagsUser> results = this.pipeline.pump(new UserServiceContext(uniqueIds))
                .through(PartialUserService.class)
                .getResult();

        this.cacheService.accept(results);

        return results;
    }

}
