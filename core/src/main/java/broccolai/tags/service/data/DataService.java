package broccolai.tags.service.data;

import broccolai.tags.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface DataService {

    @NonNull Optional<@NonNull TagsUser> getUser(@NonNull UUID uniqueId);

    void saveUser(@NonNull TagsUser user);

}
