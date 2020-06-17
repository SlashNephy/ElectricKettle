enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "ElectricKettle"

pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "build-time-tracker" -> {
                    useModule("net.rdrei.android.buildtimetracker:gradle-plugin:${requested.version}")
                }
                "com.adarshr.test-logger" -> {
                    useModule("com.adarshr:gradle-test-logger-plugin:${requested.version}")
                }
            }
        }
    }
}
