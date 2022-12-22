import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.0"
}

apply<ShadowPlugin>()

tasks {
    withType<ShadowJar> {

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
        }

        relocate(
                rootProject.group,
                "com.github.benmanes.caffeine",
                "com.typesafe.config",
                "com.zaxxer.hikari",
                "com.google.inject",
                "org.antlr",
                "org.slf4j",
                "org.jdbi",
                "org.aopalliance",
                "org.spongepowered.configurate",
                "io.leangen.geantyref",
                "cloud.commandframework",
                "net.kyori.event",
                "net.kyori.coffee",
                "org.objectweb.asm",
                "broccolai.corn"
        )

        archiveFileName.set(project.name + ".jar")
    }

    getByName("build") {
        dependsOn(withType<ShadowJar>())
    }

    runServer {
        minecraftVersion("1.19.2")
    }
}

fun ShadowJar.relocate(group: Any, vararg dependencies: String) {
    dependencies.forEach {
        val split = it.split('.')
        val name = split.last()
        relocate(it, "$group.dependencies.$name")
    }
}

dependencies {
    api(project(":tags-core"))

    compileOnly("io.papermc.paper", "paper-api", Versions.SPIGOT)
    compileOnly("me.clip", "placeholderapi", Versions.PAPI)
    compileOnly("com.github.MilkBowl", "VaultAPI", Versions.VAULT)

    api("cloud.commandframework", "cloud-paper", Versions.CLOUD)
    api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)
}
