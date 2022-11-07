import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType

class AndroidNexusRepositoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("io.github.gradle-nexus.publish-plugin")
                apply("maven-publish")
            }

            val nexusPluginExt: NexusRepositoryPluginExt = extensions.create("nordicNexusPublishing", NexusRepositoryPluginExt::class.java)

            val publishing = extensions.getByType<PublishingExtension>()
            publishing.repositories {
                maven {
                    credentials {
                        username = System.getenv("OSSR_USERNAME")
                        password = System.getenv("OSSR_PASSWORD")
                    }
                }
            }
            publishing.publications {
                this.withType(MavenPublication::class.java) {
                    if (!project.state.executed) {
                        project.afterEvaluate {
                            configureDescription(this@withType, nexusPluginExt)
                        }
                    } else {
                        configureDescription(this@withType, nexusPluginExt)
                    }
                }
            }
        }
    }

    private fun configureDescription(publication: MavenPublication, nexusPluginExt: NexusRepositoryPluginExt) {
        println("AAATESTAAA: $nexusPluginExt")

        publication.pom {
            packaging = nexusPluginExt.GROUP
            description.set(nexusPluginExt.POM_DESCRIPTION)
            url.set(nexusPluginExt.POM_URL)

            licenses {
                license {
                    distribution.set(nexusPluginExt.POM_LICENCE)
                    name.set(nexusPluginExt.POM_LICENCE_NAME)
                    url.set(nexusPluginExt.POM_LICENCE_URL)
                }
                name.set(nexusPluginExt.POM_LICENCE)

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
    var POM_LICENCE: String = "BSD-3-Clause",
    var POM_LICENCE_NAME: String = "The BSD 3-Clause License",
    var POM_LICENCE_URL: String = "http://opensource.org/licenses/BSD-3-Clause",
    var POM_DEVELOPER_ID: String = "syzi",
    var POM_DEVELOPER_NAME: String = "Sylwester Zielinski",
    var POM_DEVELOPER_EMAIL: String = "sylwester.zielinski@nordicsemi.no"
)
