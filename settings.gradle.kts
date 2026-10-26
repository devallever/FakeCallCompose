pluginManagement {
    repositories {
        maven(url="https://maven.aliyun.com/repository/gradle-plugin")
        maven(url="https://maven.aliyun.com/nexus/content/groups/public/")
        maven(url="https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven(url="https://maven.aliyun.com/repository/google")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url="https://maven.aliyun.com/repository/gradle-plugin")
        maven(url="https://maven.aliyun.com/nexus/content/groups/public/")
        maven(url="https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven(url="https://maven.aliyun.com/repository/google")
        google()
        mavenCentral()
    }
}

rootProject.name = "FakeCallCompose"
include(":app")
 