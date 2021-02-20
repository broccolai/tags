import net.kyori.indra.sonatypeSnapshots
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin

plugins {
    id("net.kyori.indra") version Versions.INDRA
    id("net.kyori.indra.checkstyle") version Versions.INDRA
}

group = "broccolai.tags"
version = "1.0"

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        maven("https://repo.broccol.ai")
    }

    dependencies {
        compileOnlyApi("com.google.guava", "guava", Versions.GUAVA)

        compileOnly("com.destroystokyo.paper", "paper-api", Versions.PAPER)
        compileOnly("me.clip", "placeholderapi", Versions.PAPI)
        compileOnly("com.github.MilkBowl", "VaultAPI", Versions.VAULT)

        api("com.google.inject", "guice", Versions.GUICE)
        implementation("com.google.inject.extensions", "guice-assistedinject", Versions.GUICE) {
            isTransitive = false
        }

        implementation("org.jdbi", "jdbi3-core", Versions.JDBI)
        implementation("com.zaxxer", "HikariCP", Versions.HIKARI)
        implementation("org.flywaydb", "flyway-core", Versions.FLYWAY)

        implementation("org.spongepowered", "configurate-hocon", Versions.CONFIGURATE)

        api("broccolai.corn", "corn-core", Versions.CORN)

        api("net.kyori", "adventure-platform-bukkit", Versions.ADVENTURE)
        implementation("net.kyori", "adventure-text-minimessage", Versions.MINI_MESSAGE) {
            isTransitive = true
        }

        api("cloud.commandframework", "cloud-paper", Versions.CLOUD)
        api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)
    }

    setupShadowJar()

    tasks {
        indra {
            gpl3OnlyLicense()

            javaVersions {
                target.set(8)
            }

            github("broccolai", "tickets") {
                ci = true
            }
        }

        processResources {
            expand("version" to project.version)
        }
    }
}

tasks {
    withType<Jar> {
        onlyIf { false }
    }
}
