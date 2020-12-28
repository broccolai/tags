package broccolai.tags.service.message;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public interface MessageService {
    Component commandAdminGive(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandAdminRemove(@NonNull Tag tag, @NonNull TagsUser target);
}
