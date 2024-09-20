import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("xyz.jpenilla.run-paper")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.gremlin-gradle")
}

repositories {
    maven("https://repo.jpenilla.xyz/snapshots/")
}

fun DependencyHandler.runtimeDownloadApi(dependencyNotation: Any) {
    api(dependencyNotation)
    runtimeDownload(dependencyNotation)
}

dependencies {
    runtimeDownloadApi(projects.tagsCore)

    compileOnly(libs.paper.api)
    compileOnly(libs.papi)
    compileOnly(libs.vault)

    runtimeDownloadApi(platform(libs.cloud.minecraft.bom))
    runtimeDownloadApi(libs.cloud.paper)
    runtimeDownloadApi(libs.cloud.extras)
    runtimeDownloadApi(libs.corn.minecraft.paper)

    runtimeDownloadApi(libs.interfaces.paper)

    runtimeDownloadApi(libs.h2)

    compileOnly(libs.miniplaceholders)
}

configurations.runtimeDownload {
    exclude("io.papermc.paper")
    exclude("net.kyori", "adventure-api")
    exclude("net.kyori", "adventure-text-minimessage")
    exclude("net.kyori", "adventure-text-serializer-plain")
    exclude("org.slf4j", "slf4j-api")
    exclude("org.ow2.asm")
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
        downloadPlugins {
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://github.com/MiniPlaceholders/MiniPlaceholders/releases/download/2.2.1/MiniPlaceholders-Paper-2.2.1.jar")
        }
    }

    withType<ShadowJar> {
        dependencies {
            include(project(":tags-core"))
            include(project(":tags-api"))
            include(dependency(libs.gremlin.get().toString()))
        }

        relocate("xyz.jpenilla.gremlin", "broccolai.tags.lib.xyz.jpenilla.gremlin")

        archiveFileName.set(project.name + ".jar")
    }

    build {
        dependsOn(shadowJar)
    }

    writeDependencies {
        repos.set(listOf(
            "https://repo.papermc.io/repository/maven-public/",
            "https://repo.broccol.ai/releases",
            "https://oss.sonatype.org/content/repositories/snapshots/",
        ))
    }
}
