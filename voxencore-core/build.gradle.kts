dependencies {
    api(project(":voxencore-api"))
    implementation(project(":voxencore-cache"))
    implementation(project(":voxencore-scheduler"))
    implementation(project(":voxencore-thread"))
    compileOnly(project(":voxencore-paper-stub"))
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
