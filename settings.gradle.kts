pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                // 將 Google 的過濾規則變得更具體
                includeGroup("com.google.dagger") // 這是給 Hilt 外掛程式用的
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
 