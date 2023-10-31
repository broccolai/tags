package broccolai.tags.api.service;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ActionService {

    boolean select(@NonNull TagsUser user, @NonNull ConstructedTag tag);

    void remove(@NonNull TagsUser user);

}
