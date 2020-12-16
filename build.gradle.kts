import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("java-library")
    id("checkstyle")
    id("com.github.johnrengelman.shadow") version("6.1.0")
}

group = "broccolai.tags"
version = "1.0-SNAPSHOT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = sourceCompatibility
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "PlaceholderAPI"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    checkstyle("ca.stellardrift:stylecheck:0.1-SNAPSHOT")

    compileOnlyApi("org.checkerframework:checker-qual:3.5.0")

    compileOnlyApi("com.google.guava:guava:21.0")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.10.9")

    api("com.google.inject:guice:5.0.0-BETA-1")
    api("org.slf4j:slf4j-nop:1.7.13")

    implementation("org.jdbi:jdbi3-core:3.17.0")
    api("com.github.ben-manes.caffeine:caffeine:2.8.6")

    implementation("com.zaxxer:HikariCP:3.4.5")

    implementation("org.flywaydb:flyway-core:7.3.1")

    implementation("org.spongepowered:configurate-hocon:4.0.0")

    api("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT") {
        isTransitive = true
    }

    api("cloud.commandframework:cloud-paper:1.2.0")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split('.')
                val name = split.last();
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
        }

        relocates(
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
                "net.kyori.adventure",
                "net.kyori.examination"
        )

        archiveFileName.set(project.name + ".jar")
        minimize()
    }

    checkstyle {
        val configRoot = File(rootProject.projectDir, ".checkstyle")
        toolVersion = "8.34"
        configDirectory.set(configRoot)
        configProperties["basedir"] = configRoot.absolutePath
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}
