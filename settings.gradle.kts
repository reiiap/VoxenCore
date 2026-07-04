pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "VoxenCore"

include(
    "voxencore-api",
    "voxencore-core",
    "voxencore-cache",
    "voxencore-database",
    "voxencore-packet",
    "voxencore-player",
    "voxencore-gui",
    "voxencore-item",
    "voxencore-hologram",
    "voxencore-npc",
    "voxencore-menu",
    "voxencore-placeholder",
    "voxencore-economy",
    "voxencore-resourcepack",
    "voxencore-contract",
    "voxencore-dungeon",
    "voxencore-fishing",
    "voxencore-announcement",
    "voxencore-scoreboard",
    "voxencore-tab",
    "voxencore-scheduler",
    "voxencore-thread",
    "voxencore-config",
    "voxencore-utils",
    "voxencore-paper-stub"
)
