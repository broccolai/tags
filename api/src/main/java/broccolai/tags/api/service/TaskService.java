package broccolai.tags.api.service;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface TaskService {
    void sync(@NonNull Runnable runnable);

    void async(@NonNull Runnable runnable);
}
