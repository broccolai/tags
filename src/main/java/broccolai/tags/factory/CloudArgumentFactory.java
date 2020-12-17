package broccolai.tags.factory;

import broccolai.tags.commands.arguments.TagArgument;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CloudArgumentFactory {
  @NonNull TagArgument tag(@Assisted("name") @NonNull String name);
}
