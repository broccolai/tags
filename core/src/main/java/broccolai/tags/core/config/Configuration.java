package broccolai.tags.core.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public interface Configuration {
    <N extends ScopedConfigurationNode<N>> void saveTo(@NonNull N node) throws SerializationException;
}
