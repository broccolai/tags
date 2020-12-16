package broccolai.tags.service.user;

import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.data.DataService;
import broccolai.tags.service.tags.TagsService;
import broccolai.tags.service.user.impl.UserCacheService;
import broccolai.tags.service.user.impl.UserCreateService;
import broccolai.tags.service.user.impl.UserSQLService;
import cloud.commandframework.services.ServicePipeline;
import com.google.inject.Inject;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class UserPipeline {

    private final ServicePipeline pipeline = ServicePipeline.builder().build();
    private final UserCacheService cacheService;

    @Inject
    public UserPipeline(final DataService dataService, final TagsService tagsService) {
        this.cacheService = new UserCacheService(tagsService);

        this.pipeline.registerServiceType(TypeToken.get(UserService.class), new UserCreateService())
                .registerServiceImplementation(UserService.class, new UserSQLService(dataService), Collections.emptyList())
                .registerServiceImplementation(UserService.class, this.cacheService, Collections.emptyList());
    }

    public TagsUser get(final @NonNull UUID uniqueId) {
        return this.get(Collections.singletonList(uniqueId)).get(uniqueId);
    }

    public @NonNull Map<UUID, TagsUser> get(final @NonNull Collection<UUID> uniqueIds) {
        Map<UUID, TagsUser> results = this.pipeline.pump(new UserServiceContext(uniqueIds))
                .through(UserService.class)
                .getResult();

        this.cacheService.accept(results);

        return results;
    }

}
