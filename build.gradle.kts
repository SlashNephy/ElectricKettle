import com.adarshr.gradle.testlogger.theme.ThemeType

group = "blue.starry"
version = "0.0.1"

plugins {
    kotlin("js") version "1.6.21" apply false

    // For testing
    id("build-time-tracker") version "0.11.1"
    id("com.adarshr.test-logger") version "2.0.0"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.js")
}

/*
 * Tests
 */

buildtimetracker {
    reporters {
        register("summary") {
            options["ordered"] = "true"
            options["threshold"] = "50"
            options["barstyle"] = "unicode"
            options["shortenTaskNames"] = "false"
        }
    }
}

testlogger {
    theme = ThemeType.MOCHA

    showFullStackTraces = true
}
