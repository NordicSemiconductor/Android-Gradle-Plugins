/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
                        name.set("BSD-3-Clause")
                        url.set("http://opensource.org/licenses/BSD-3-Clause")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("syzi")
                        name.set("Sylwester Zieliński")
                        email.set("sylwester.zielinski@nordicsemi.no")
                        organization.set("Nordic Semiconductor ASA")
                        organizationUrl.set("https://www.nordicsemi.com")
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
