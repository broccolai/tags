package broccolai.tags.core.platform;

import broccolai.tags.core.commands.PluginCommand;
import broccolai.tags.core.commands.TagsAdminCommand;
import broccolai.tags.core.commands.TagsCommand;
import broccolai.tags.core.inject.ConfigurationModule;
import broccolai.tags.core.inject.PluginModule;
import broccolai.tags.core.inject.ServiceModule;
import broccolai.tags.core.inject.factory.CloudArgumentFactoryModule;
import broccolai.tags.core.util.ArrayUtilities;
import com.google.inject.Module;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TagsPlatform {
    @NonNull Module @NonNull [] STANDARD_MODULES = new Module[]{
            new ConfigurationModule(),
            new PluginModule(),
            new CloudArgumentFactoryModule(),
            new ServiceModule()
    };

    @NonNull Class<? extends PluginCommand>[] COMMANDS = ArrayUtilities.create(
            TagsCommand.class,
            TagsAdminCommand.class
    );
}
