package broccolai.tags.core.config;

import broccolai.tags.core.data.StorageMethod;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StorageConfiguration {

    @Setting
    @Comment("Storage method. Current options are: LUCKPERMS, H2")
    public StorageMethod storageMethod = StorageMethod.H2;

}
