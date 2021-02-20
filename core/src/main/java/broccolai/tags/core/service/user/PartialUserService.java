package broccolai.tags.core.service.user;

import broccolai.tags.api.model.user.TagsUser;
import cloud.commandframework.services.types.PartialResultService;

import java.util.UUID;

public interface PartialUserService extends PartialResultService<UUID, TagsUser, UserServiceContext> {

}
