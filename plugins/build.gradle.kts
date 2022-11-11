/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `version-catalog`
    `kotlin-dsl`
    `maven-publish`
    signing
}
apply(from = "../gradle/git-tag-version.gradle")

val getVersionNameFromTags: groovy.lang.Closure<String> by ext

group = "no.nordicsemi.android.gradle"
version = getVersionNameFromTags()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("application.compose") {
            id = "no.nordicsemi.android.gradle.application.compose"
            displayName = "Application with Compose"
            description = "Application plugin extension with Compose feature enabled"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("application") {
            id = "no.nordicsemi.android.gradle.application"
            displayName = "Standalone Application configuration"
            description = "Application plugin extension for internal releases"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("library.compose") {
            id = "no.nordicsemi.android.gradle.library.compose"
            displayName = "Library with Compose"
            description = "Library plugin extension with Compose feature enabled"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("library") {
            id = "no.nordicsemi.android.gradle.library"
            displayName = "Standalone library configuration"
            description = "Library plugin extension for internal releases"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("feature") {
            id = "no.nordicsemi.android.gradle.feature"
            displayName = "Feature plugin"
            description = "UI feature plugin with Hilt & Compose"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = "no.nordicsemi.android.gradle.hilt"
            displayName = "Hilt plugin"
            description = "Plugin enabling Hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("nexus") {
            id = "no.nordicsemi.android.gradle.nexus"
            displayName = "Nexus plugin"
            description = "Plugin creating a task for publishing to Nexus repository."
            implementationClass = "AndroidNexusRepositoryPlugin"
        }
    }
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("libs") {
            from(components["versionCatalog"])

            groupId = "no.nordicsemi.android.gradle"
            artifactId = "version-catalog"
            version = getVersionNameFromTags()

            pom {
                name.set("Nordic version catalog for Android")
                packaging = "toml"
                description.set("Nordic version catalog for Android")
                url.set("https://github.com/NordicSemiconductor/Android-Gradle-Plugins")

                scm {
                    url.set("https://github.com/NordicSemiconductor/Android-Gradle-Plugins")
                    connection.set("scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git")
                    developerConnection.set("scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git")
                }

                licenses {
                    license {
                        name.set("The BSD 3-Clause License")
                        url.set("http://opensource.org/licenses/BSD-3-Clause")
                    }
                }

                developers {
                    developer {
                        id.set("syzi")
                        name.set("Sylwester Zielinski")
                        email.set("sylwester.zielinski@nordicsemi.no")
                    }
                }
            }
        }
    }
    publications {
        create<MavenPublication>("plugins") {
            from(components["java"])

            groupId = "no.nordicsemi.android.gradle"
            artifactId = "plugins"
            version = getVersionNameFromTags()

            artifact(sourcesJar.get())

            pom {
                name.set("Nordic Gradle plugins for Android")
                packaging = "jar"
                description.set("Nordic Gradle plugins for Android")
                url.set("https://github.com/NordicSemiconductor/Android-Gradle-Plugins")

                scm {
                    url.set("https://github.com/NordicSemiconductor/Android-Gradle-Plugins")
                    connection.set("scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git")
                    developerConnection.set("scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git")
                }

                licenses {
                    license {
                        name.set("The BSD 3-Clause License")
                        url.set("http://opensource.org/licenses/BSD-3-Clause")
                    }
                }

                developers {
                    developer {
                        id.set("syzi")
                        name.set("Sylwester Zielinski")
                        email.set("sylwester.zielinski@nordicsemi.no")
                    }
                }
            }
        }
    }
}

ext["signing.keyId"] = System.getenv("GPG_SIGNING_KEY")
ext["signing.password"] = System.getenv("GPG_PASSWORD")
ext["signing.secretKeyRingFile"] = "../sec.gpg"

signing {
    sign(publishing.publications)
}
