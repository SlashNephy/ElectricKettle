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

repositories {
    mavenCentral()
    jcenter()

    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/nephyproject/stable")
    maven("https://dl.bintray.com/nephyproject/dev")
}

kotlin {
    target {
        browser {
            testTask {
                enabled = false
            }

            dceTask {
                // workaround for https://kotlinlang.org/docs/reference/javascript-dce.html#known-issue-dce-and-ktor.
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
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

                moduleKind = "amd"
            }
        }
    }

    sourceSets {
        main {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.1")

                // React
                implementation("org.jetbrains:kotlin-react:16.13.0-pre.94-kotlin-1.3.70")
                implementation("org.jetbrains:kotlin-react-dom:16.13.0-pre.94-kotlin-1.3.70")
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))

                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.94-kotlin-1.3.70")
                implementation(npm("styled-components"))
                implementation(npm("inline-style-prefixer"))

                implementation(npm("react-player"))
                implementation(npm("react-share"))

                implementation("blue.starry:penicillin-js:5.0.1-eap-4")
                runtimeOnly(npm("crypto-js"))
                runtimeOnly(npm("text-encoding"))
                runtimeOnly(npm("bufferutil"))
                runtimeOnly(npm("utf-8-validate"))
                runtimeOnly(npm("abort-controller"))
                runtimeOnly(npm("fs"))
                implementation("io.github.microutils:kotlin-logging-js:1.7.9")
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
