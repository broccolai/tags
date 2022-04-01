package broccolai.tags.core.model.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface LocaleEntry {

    Component asComponent(TagResolver... templates);

}
