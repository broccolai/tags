package broccolai.tags.config;

import broccolai.tags.data.StorageType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StorageConfiguration {

    @Setting
    @Comment("Storage Type. Current options are: SQLITE")
    public StorageType storageType = StorageType.SQLITE;

}
