dependencies {
    api(project(":tags-api"))
    compileOnlyApi(libs.guava)
    compileOnly(libs.luckperms)

    implementation(libs.guice.assisted) {
        isTransitive = false
    }

    api(libs.jdbi.core)
    implementation(libs.hikari)
    implementation(libs.flyway)

    implementation(libs.configurate)

    api(libs.corn.core)

    api(libs.cloud.core)
    api(libs.event.asm)
    api(libs.coffee)
}
