package broccolai.tags.core.service.message;

import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.config.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;
    private final UserService userService;

    @Inject
    public MiniMessageService(
            final @NonNull LocaleConfiguration locale,
            final @NonNull UserService userService
    ) {
        this.locale = locale;
        this.userService = userService;
    }

    @Override
    public Template prefix() {
        return Template.of("prefix", this.locale.prefix.asComponent());
    }

    @Override
    public Component commandSelect(final @NonNull Tag tag) {
        Template tagComponent = Template.of("tag", tag.component());

        return this.locale.commands.player.select.asComponent(this.prefix(), tagComponent);
    }

    @Override
    public Component commandList(final @NonNull Collection<Tag> tags) {
        Component component = this.locale.commands.player.list.asComponent(this.prefix());

        for (Tag tag : tags) {
            Template nameTemplate = Template.of("name", tag.name());
            Template tagComponent = Template.of("tag", tag.component());

            component = component.append(this.locale.commands.player.listEntry.asComponent(nameTemplate, tagComponent));
        }

        return component;
    }

    @Override
    public Component commandInfo(final @NonNull Tag tag) {
        Template nameTemplate = Template.of("name", tag.name());
        Template tagTemplate = Template.of("tag", tag.component());
        Template reasonTemplate = Template.of("reason", tag.reason());

        return this.locale.commands.player.info.asComponent(this.prefix(), nameTemplate, tagTemplate, reasonTemplate);
    }

    @Override
    public Component commandAdminGive(
            final @NonNull Tag tag,
            final @NonNull TagsUser target
    ) {
        Template tagComponent = Template.of("tag", tag.component());
        Template targetComponent = Template.of("target", this.nameFromUser(target));

        return this.locale.commands.admin.give.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminRemove(
            final @NonNull Tag tag,
            final @NonNull TagsUser target
    ) {
        Template tagComponent = Template.of("tag", tag.component());
        Template targetComponent = Template.of("target", this.nameFromUser(target));

        return this.locale.commands.admin.remove.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminList(final @NonNull Collection<Tag> tags) {
        Component component = this.locale.commands.admin.list.asComponent(this.prefix());

        for (Tag tag : tags) {
            Template tagComponent = Template.of("tag", tag.component());

            component = component.append(this.locale.commands.admin.listEntry.asComponent(tagComponent));
        }

        return component;
    }

    @Override
    public Component commandErrorUserNotFound(final @NonNull String input) {
        Template inputTemplate = Template.of("input", input);

        return this.locale.commands.error.userNotFound.asComponent(this.prefix(), inputTemplate);
    }

    @Override
    public Component commandErrorTagNotFound(final @NonNull String input) {
        Template inputTemplate = Template.of("input", input);

        return this.locale.commands.error.tagNotFound.asComponent(this.prefix(), inputTemplate);
    }

    //todo: Add user to TagUser object?
    private String nameFromUser(final @NonNull TagsUser user) {
        return this.userService.name(user);
    }

}
