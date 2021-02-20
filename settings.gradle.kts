rootProject.name = "tags"

//include("api")
//include("core")
//
//project(":api").name = "tags-api"
//project(":core").name = "tags-core"

use("api", "core", "bukkit")

fun use(vararg names: String) {
    for (name in names) {
        include(name)
        project(":$name").name = "${rootProject.name}-$name"
    }
}
