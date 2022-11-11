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
    `maven-publish`
    signing
}
apply(from = "../gradle/git-tag-version.gradle")

val getVersionNameFromTags: groovy.lang.Closure<String> by ext

group = "no.nordicsemi.android.gradle"
version = getVersionNameFromTags()

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
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
}

ext["signing.keyId"] = System.getenv("GPG_SIGNING_KEY")
ext["signing.password"] = System.getenv("GPG_PASSWORD")
ext["signing.secretKeyRingFile"] = "../sec.gpg"

signing {
    sign(publishing.publications)
}
