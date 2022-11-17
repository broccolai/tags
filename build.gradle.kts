import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin

plugins {
    id("net.kyori.indra") version Versions.INDRA
    id("net.kyori.indra.publishing") version Versions.INDRA
    id("net.kyori.indra.checkstyle") version Versions.INDRA
    id("com.github.ben-manes.versions") version "0.44.0"
}

group = "love.broccolai.tags"
version = "2.0.0-SNAPSHOT"

subprojects {
    apply<IndraPlugin>()
    apply<IndraPublishingPlugin>()
    apply<IndraCheckstylePlugin>()

    repositories {
        mavenCentral()
        sonatype.ossSnapshots()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        maven("https://repo.broccol.ai")
    }

    tasks {
        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target(17)
            }

            github("broccolai", "tags") {
                ci(true)
                publishing(true)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }
    }
}

tasks {
    withType<Jar> {
        onlyIf { false }
    }
}
