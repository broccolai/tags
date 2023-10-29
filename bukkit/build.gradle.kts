import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.gremlin-gradle")
}

repositories {
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    api(project(":tags-core"))
    runtimeDownload(project(":tags-core"))

    compileOnly(libs.paper.api)
    compileOnly(libs.papi)
    compileOnly(libs.vault)

    api(libs.cloud.paper)
    runtimeDownload(libs.cloud.paper)
    api(libs.cloud.extras)
    runtimeDownload(libs.cloud.extras)
    api(libs.corn.minecraft.paper)
    runtimeDownload(libs.corn.minecraft.paper)

    api(libs.interfaces.paper)
    runtimeDownload(libs.interfaces.paper)

    api(libs.h2)
    runtimeDownload(libs.h2)
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
        downloadPlugins {
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
        }
    }

    withType<ShadowJar> {
        dependencies {
            include(project(":tags-core"))
            include(project(":tags-api"))
            include(dependency(libs.gremlin.get().toString()))
        }

        archiveFileName.set(project.name + ".jar")
    }

    build {
        dependsOn(shadowJar)
    }

    writeDependencies {
        repos.set(listOf(
            "https://repo.papermc.io/repository/maven-public/",
            "https://repo.broccol.ai/releases",
            "https://repo.jpenilla.xyz/snapshots/",
        ))
    }
}
