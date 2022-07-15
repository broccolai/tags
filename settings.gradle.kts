plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
}

rootProject.name = "tags"


use("api", "core", "bukkit")

fun use(vararg names: String) {
    for (name in names) {
        include(name)
        project(":$name").name = "${rootProject.name}-$name"
    }
}
