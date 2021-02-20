package broccolai.tags.core.model.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;

public interface LocaleEntry {
    Component asComponent(Template... templates);
}
