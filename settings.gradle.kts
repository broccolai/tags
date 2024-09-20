pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.jpenilla.xyz/snapshots/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.1.0"
}

rootProject.name = "tags"

use("api", "core", "paper")

fun use(vararg names: String) {
    for (name in names) {
        include(name)
        project(":$name").name = "${rootProject.name}-$name"
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
