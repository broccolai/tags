package broccolai.tags.service.user;

import broccolai.tags.model.user.TagsUser;
import cloud.commandframework.services.types.PartialResultService;

import java.util.UUID;

public interface UserService extends PartialResultService<UUID, TagsUser, UserServiceContext> {

}
