package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.user.TagsUser;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface DataService extends Service {

    @NonNull Optional<@NonNull TagsUser> loadUser(@NonNull UUID uniqueId);

    void saveUser(@NonNull TagsUser user);

}
