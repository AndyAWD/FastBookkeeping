pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                // 這一行就涵蓋了 Hilt、KSP 以及那個找不到的 testing platform 依賴
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal() // KSP 外掛程式將會在這裡被正確找到
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FastBookkeeping"
include(":app")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FastBookkeeping"
include(":app")
 