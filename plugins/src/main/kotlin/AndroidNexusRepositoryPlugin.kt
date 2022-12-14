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

import com.android.build.gradle.LibraryExtension
import no.nordicsemi.android.buildlogic.getVersionNameFromTags
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension

class AndroidNexusRepositoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("maven-publish")
                apply("signing")
            }

            val nexusPluginExt: NexusRepositoryPluginExt = extensions.create("nordicNexusPublishing", NexusRepositoryPluginExt::class.java)
            val library = extensions.getByType<LibraryExtension>()

            extra.set("signing.keyId", System.getenv("GPG_SIGNING_KEY"))
            extra.set("signing.password", System.getenv("GPG_PASSWORD"))
            extra.set("signing.secretKeyRingFile", "../sec.gpg")

            library.publishing {
                singleVariant("sources") {
                    withSourcesJar()
                }
            }

            project.afterEvaluate {
                project.tasks.create("androidSourcesJar", Jar::class.java) {
                    afterEvaluate {
                        archiveClassifier.set("sources")
                        from(library.sourceSets.getByName("main").java.srcDirs)
                    }
                }

                project.configurePublishingExtension(nexusPluginExt)
            }
        }
    }

    private fun Project.configurePublishingExtension(nexusPluginExt: NexusRepositoryPluginExt) {
        val publishing = extensions.getByType(PublishingExtension::class.java)
        val signing = extensions.getByType<SigningExtension>()

        val androidSourcesJar = tasks.getByName("androidSourcesJar") as AbstractArchiveTask

        artifacts {
            add("archives", androidSourcesJar)
        }

        publishing.repositories {
            maven {
                setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = System.getenv("OSSR_USERNAME")
                    password = System.getenv("OSSR_PASSWORD")
                }
            }
        }
        publishing.publications {
            this.register("mavenPublication", MavenPublication::class.java) {
                from(components["release"])
                if (!project.state.executed) {
                    project.afterEvaluate {
                        configureDescription(this@register, nexusPluginExt, androidSourcesJar)
                    }
                } else {
                    configureDescription(this@register, nexusPluginExt, androidSourcesJar)
                }
            }
        }

        signing.sign(publishing.publications.getByName("mavenPublication"))
    }

    private fun Project.configureDescription(
        publication: MavenPublication,
        nexusPluginExt: NexusRepositoryPluginExt,
        androidSourcesJar: AbstractArchiveTask
    ) {
        publication.artifactId = nexusPluginExt.POM_ARTIFACT_ID
        publication.groupId = nexusPluginExt.GROUP
        publication.version = getVersionNameFromTags()

        publication.artifact(androidSourcesJar)

        publication.pom {
            this.name.set(nexusPluginExt.POM_NAME)

            packaging = nexusPluginExt.POM_PACKAGING
            description.set(nexusPluginExt.POM_DESCRIPTION)
            url.set(nexusPluginExt.POM_URL)

            licenses {
                license {
                    distribution.set(nexusPluginExt.POM_LICENCE)
                    name.set(nexusPluginExt.POM_LICENCE_NAME)
                    url.set(nexusPluginExt.POM_LICENCE_URL)
                    name.set(nexusPluginExt.POM_LICENCE)
                }
            }
            scm {
                url.set(nexusPluginExt.POM_SCM_URL)
                connection.set(nexusPluginExt.POM_SCM_CONNECTION)
                developerConnection.set(nexusPluginExt.POM_SCM_DEV_CONNECTION)
            }
            developers {
                developer {
                    id.set(nexusPluginExt.POM_DEVELOPER_ID)
                    name.set(nexusPluginExt.POM_DEVELOPER_NAME)
                    email.set(nexusPluginExt.POM_DEVELOPER_EMAIL)
                    organization.set(nexusPluginExt.POM_DEVELOPER_ORG)
                    organizationUrl.set(nexusPluginExt.POM_DEVELOPER_ORG_URL)
                }
            }
        }
    }
}

abstract class NexusRepositoryPluginExt(
    var POM_ARTIFACT_ID: String = "",
    var POM_NAME: String = "",
    var POM_PACKAGING: String = "aar",

    var GROUP: String = "no.nordicsemi.android",
    var POM_DESCRIPTION: String = "Nordic Android Common Libraries",
    var POM_URL: String = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins",
    var POM_SCM_URL: String = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins",
    var POM_SCM_CONNECTION: String = "scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git",
    var POM_SCM_DEV_CONNECTION: String = "scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git",
    // License
    var POM_LICENCE: String = "BSD-3-Clause",
    var POM_LICENCE_NAME: String = "The BSD 3-Clause License",
    var POM_LICENCE_URL: String = "http://opensource.org/licenses/BSD-3-Clause",
    // Developer
    var POM_DEVELOPER_ID: String = "mag",
    var POM_DEVELOPER_NAME: String = "Mobile Applications Group",
    var POM_DEVELOPER_EMAIL: String = "mag@nordicsemi.no",
    var POM_DEVELOPER_ORG: String = "Nordic Semiconductor ASA",
    var POM_DEVELOPER_ORG_URL: String = "https://www.nordicsemi.com"
)
