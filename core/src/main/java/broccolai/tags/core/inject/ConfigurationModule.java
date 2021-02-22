package broccolai.tags.core.inject;

import broccolai.tags.core.config.Configuration;
import broccolai.tags.core.config.LocaleConfiguration;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.config.serializers.LocaleEntrySerializer;
import broccolai.tags.core.model.locale.LocaleEntry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.coffee.functional.function.exceptional.Function2E;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

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
            return this.configuration(file, (loader, node) -> {
                loader.save(updateNode(node));
                return MainConfiguration.loadFrom(node);
            });
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
            return this.configuration(file, (loader, node) -> {
                return LocaleConfiguration.loadFrom(node);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private <T extends Configuration> @NonNull T configuration(
            final @NonNull File file,
            final @NonNull Function2E<ConfigurationLoader<?>, ConfigurationNode, T, ConfigurateException> function
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
        T config = function.apply(loader, node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

    public static <N extends ConfigurationNode> N updateNode(final N node) throws ConfigurateException {
        if (!node.virtual()) { // we only want to migrate existing data
            final ConfigurationTransformation.Versioned trans = create();
            trans.apply(node);
        }
        return node;
    }

    public static ConfigurationTransformation initialTransform() {
        return ConfigurationTransformation.builder()
                .addAction(NodePath.path("storage", "storage-type"), TransformAction.remove())
                .build();
    }

    public static ConfigurationTransformation.Versioned create() {
        return ConfigurationTransformation.versionedBuilder()
                .addVersion(0, initialTransform())
                .build();
    }

}
