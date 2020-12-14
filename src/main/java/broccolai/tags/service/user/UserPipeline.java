package broccolai.tags.service.user;

import broccolai.tags.model.TagsUser;
import broccolai.tags.service.data.DataService;
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

    @Inject
    public UserPipeline(final DataService dataService) {
        this.pipeline.registerServiceType(TypeToken.get(UserService.class), new UserCreateService())
                .registerServiceImplementation(UserService.class, new UserSQLService(dataService), Collections.emptyList())
                .registerServiceImplementation(UserService.class, new UserCacheService(), Collections.emptyList());
    }

    public TagsUser get(final @NonNull UUID uniqueId) {
        return this.get(Collections.singletonList(uniqueId)).get(uniqueId);
    }

    public @NonNull Map<UUID, TagsUser> get(final @NonNull Collection<UUID> uniqueIds) {
        return this.pipeline.pump(new UserServiceContext(uniqueIds))
                .through(UserService.class)
                .getResult();
    }

}
