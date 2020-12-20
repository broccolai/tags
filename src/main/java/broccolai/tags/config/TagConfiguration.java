package broccolai.tags.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@NonNull
public final class TagConfiguration {

    @Setting
    @Comment("Tags unique id")
    public int id;

    @Setting
    @Comment("Readable name for players")
    public String name;

    @Setting
    @Comment("MiniMessage component to display")
    public String component;

    public TagConfiguration() {
    }

    public TagConfiguration(final int id, final String name, final String component) {
        this.id = id;
        this.name = name;
        this.component = component;
    }

}
