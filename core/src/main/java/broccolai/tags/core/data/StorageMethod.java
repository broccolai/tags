package broccolai.tags.core.data;

import broccolai.tags.api.service.DataService;
import broccolai.tags.core.service.data.H2DataService;
import broccolai.tags.core.service.data.LPDataService;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum StorageMethod {
    H2(H2DataService.class),
    LUCKPERMS(LPDataService.class);

    private final Class<? extends DataService> clazz;

    StorageMethod(final Class<? extends DataService> clazz) {
        this.clazz = clazz;
    }

    public @NonNull Class<? extends DataService> clazz() {
        return this.clazz;
    }
}
