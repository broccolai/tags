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
    @Comment("Whether to hide the tag from public displays")
    public boolean secret = false;

    @Setting
    @Comment("MiniMessage component to display")
    public String component;

    @Setting
    @Comment("How to obtain this tag, can be null")
    public String reason = null;

    public TagConfiguration() {
    }

    public TagConfiguration(final int id, final String name, final boolean secret, final String component, final String reason) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.component = component;
        this.reason = reason;
    }

}
