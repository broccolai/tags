package broccolai.tags.core.inject;

import broccolai.tags.core.config.Configuration;
import broccolai.tags.core.config.LocaleConfiguration;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.config.serializers.LocaleEntrySerializer;
import broccolai.tags.core.model.locale.LocaleEntry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.coffee.functional.function.exceptional.Function1E;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;

public final class ConfigurationModule extends AbstractModule {

    @Provides
    @Singleton
    public @NonNull MainConfiguration provideMainConfiguration(
            final @NonNull File folder
    ) {
        File file = new File(folder, "config.conf");

        try {
            return this.configuration(file, MainConfiguration::loadFrom);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public @NonNull LocaleConfiguration provideLocaleConfiguration(
            final @NonNull File folder
    ) {
        File file = new File(folder, "locale.conf");

        try {
            return this.configuration(file, LocaleConfiguration::loadFrom);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private <T extends Configuration> @NonNull T configuration(
            final @NonNull File file,
            final @NonNull Function1E<ConfigurationNode, T, SerializationException> function
    ) throws IOException {
        file.createNewFile();

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(opts -> {
                    opts = opts.serializers(builder -> {
                        builder.register(LocaleEntry.class, LocaleEntrySerializer.INSTANCE);
                    });

                    return opts.shouldCopyDefaults(true);
                })
                .file(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        T config = function.apply(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

}
