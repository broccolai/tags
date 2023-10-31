package broccolai.tags.api.model.tag;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

//todo(josh): split tag into an interface and hide this in the implementation
//            and remove configurate as a dependency in for api project
@ConfigSerializable
public record TagDisplayInformation(
        @NonNull String material,
        @Nullable Integer customModelData
) {
}
