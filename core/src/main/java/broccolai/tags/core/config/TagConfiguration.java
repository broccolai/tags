package broccolai.tags.core.config;

import broccolai.tags.api.model.tag.TagDisplayInformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@NonNull
public final class TagConfiguration {

    @Setting
    public int id;

    @Setting
    public String name;

    @Setting
    public boolean secret = false;

    @Setting
    public String component;

    @Setting
    public String reason = null;

    @Setting
    public TagDisplayInformation displayInformation = new TagDisplayInformation(
            "stick",
            0
    );

    public TagConfiguration() {
    }

    public TagConfiguration(
            final int id,
            final String name,
            final boolean secret,
            final String component,
            final String reason,
            final TagDisplayInformation displayInformation
    ) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.component = component;
        this.reason = reason;
        this.displayInformation = displayInformation;
    }

}
