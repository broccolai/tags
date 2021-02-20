package broccolai.tags.api.service;

import broccolai.tags.api.model.Permissible;
import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.user.TagsUser;

public interface PermissionService extends Service {
    boolean has(TagsUser user, Permissible permissible);

    void grant(TagsUser user, Permissible permissible);

    void remove(TagsUser user, Permissible permissible);
}
