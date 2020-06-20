/*
 * The MIT License (MIT)
 *
 *     Copyright (c) 2020 StarryBlueSky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.gradle.internal.os.OperatingSystem

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    target {
        nodejs {
            testTask {
                enabled = false
            }
        }

        compilations.all {
            kotlinOptions {
                apiVersion = "1.3"
                languageVersion = "1.3"
                verbose = true

                main = "call"
                metaInfo = true
                sourceMap = true
                moduleKind = "commonjs"
            }
        }
    }

    sourceSets {
        main {
            dependencies {
                implementation(kotlin("stdlib-js"))

                implementation(npm("electron", "9.0.4"))
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
 * Tasks
 */

tasks {
    register<Copy>("copyAppResources") {
        dependsOn("build")

        from(kotlin.sourceSets["main"].resources.sourceDirectories)
        into("${parent!!.buildDir}/js/packages/${parent!!.name}-${project.name}/kotlin")
    }

    register<Copy>("copyFrontResources") {
        dependsOn(":front:build")

        from(project(":front").buildDir.resolve("distributions"))
        into("${parent!!.buildDir}/js/packages/${parent!!.name}-${project.name}/kotlin")
    }

    register<Exec>("runElectron") {
        dependsOn("copyAppResources", "copyFrontResources")

        workingDir = parent!!.buildDir

        executable = when (OperatingSystem.current()) {
            OperatingSystem.WINDOWS -> {
                "${parent!!.buildDir.absolutePath}/js/node_modules/electron/dist/electron.exe"
            }
            else -> {
                "js/node_modules/electron/dist/electron"
            }
        }
        args("js/packages/${parent!!.name}-${project.name}")

        doFirst {
            println(commandLine)
        }
    }
}
