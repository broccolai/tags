package broccolai.tags.bukkit;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class PaperTagsLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(
            new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build()
        );

        resolver.addRepository(
            new RemoteRepository.Builder("broccoli", "default", "https://repo.broccol.ai/releases").build()
        );

        for (String serialized : this.readDependencies()) {
            Dependency dependency = new Dependency(new DefaultArtifact(serialized), null);
            resolver.addDependency(dependency);
        }

        classpathBuilder.addLibrary(resolver);
    }

    private List<String> readDependencies() {
        InputStream inputStream = getClass().getResourceAsStream("/libraries.txt");

        try (
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)
        ) {
            return reader.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
