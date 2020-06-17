import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.internal.os.OperatingSystem

group = "blue.starry"
version = "0.0.1"

object ThirdpartyVersion {
    const val electron = "9.0.4"
    const val Penicillin = "5.0.1-eap-4"
    const val kotlinx_html = "0.7.1"

    // For logging
    const val KotlinLogging = "1.7.9"
}

plugins {
    kotlin("js") version "1.3.72"

    // For testing
    id("build-time-tracker") version "0.11.1"
    id("com.adarshr.test-logger") version "2.0.0"
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/nephyproject/stable")
    maven(url = "https://dl.bintray.com/nephyproject/dev")
}

kotlin {
    target {
        browser {
            testTask {
                enabled = false
            }
            webpackTask {
                enabled = false
            }
        }

        compilations.all {
            kotlinOptions {
                apiVersion = "1.3"
                languageVersion = "1.3"
                verbose = true

                moduleKind = "commonjs"
                main = "call"
                metaInfo = true
                sourceMap = true
            }
        }
    }

    sourceSets {
        main {
            dependencies {
                implementation(kotlin("stdlib-js"))

                implementation(npm("electron", ThirdpartyVersion.electron))

                implementation("org.jetbrains.kotlinx:kotlinx-html-js:${ThirdpartyVersion.kotlinx_html}")

                implementation("blue.starry:penicillin-js:${ThirdpartyVersion.Penicillin}")
                implementation("io.github.microutils:kotlin-logging-js:${ThirdpartyVersion.KotlinLogging}")
            }
        }
        test {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }

    sourceSets.all {
        languageSettings.progressiveMode = true
        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
    }
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

/*
 * Tasks
 */

tasks {
    register<Copy>("copyResources") {
        from(kotlin.sourceSets["main"].resources.sourceDirectories)
        into("$buildDir/js/packages/${project.name}/kotlin")
    }

    register<Exec>("runElectron") {
        dependsOn("build", "copyResources")

        workingDir = buildDir

        commandLine = when (OperatingSystem.current()) {
            OperatingSystem.WINDOWS -> {
                listOf("${buildDir.absolutePath}/js/node_modules/electron/dist/electron.exe")
            }
            else -> {
                listOf("js/node_modules/electron/dist/electron")
            }
        }
        args = listOf("js/packages/${project.name}")

        doFirst {
            println(commandLine)
        }
    }
}
