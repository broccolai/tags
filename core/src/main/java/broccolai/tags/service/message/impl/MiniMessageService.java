package broccolai.tags.service.message.impl;

import broccolai.tags.config.LocaleConfiguration;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.message.MessageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;

    @Inject
    public MiniMessageService(final @NonNull LocaleConfiguration locale) {
        this.locale = locale;
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
        return Bukkit.getOfflinePlayer(user.uuid()).getName();
    }

}
