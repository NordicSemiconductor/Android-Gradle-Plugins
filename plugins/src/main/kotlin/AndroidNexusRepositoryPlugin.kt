import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType

class AndroidNexusRepositoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("maven-publish")
            }

            val publishing = extensions.getByType<PublishingExtension>()
            publishing.publications {
                this.withType(MavenPublication::class.java) {
                    if (!project.state.executed) {
                        project.afterEvaluate {
                            configureDescription(this@withType, this)
                        }
                    } else {
                        configureDescription(this@withType, project)
                    }
                }
            }
        }
    }

    private fun configureDescription(publication: MavenPublication, p: Project) {
        publication.pom {
            packaging = SonatypeObject.GROUP
            description.set(SonatypeObject.POM_DESCRIPTION)
            url.set(SonatypeObject.POM_URL)

            licenses {
                license {
                    distribution.set(SonatypeObject.POM_LICENCE)
                    name.set(SonatypeObject.POM_LICENCE_NAME)
                    url.set(SonatypeObject.POM_LICENCE_URL)
                }
                name.set(SonatypeObject.POM_LICENCE)

            }
            scm {
                url.set(SonatypeObject.POM_SCM_URL)
                connection.set(SonatypeObject.POM_SCM_CONNECTION)
                developerConnection.set(SonatypeObject.POM_SCM_DEV_CONNECTION)
            }
            developers {
                developer {
                    id.set(SonatypeObject.POM_DEVELOPER_ID)
                    name.set(SonatypeObject.POM_DEVELOPER_NAME)
                    email.set(SonatypeObject.POM_DEVELOPER_EMAIL)
                }
            }
        }
    }
}

object SonatypeObject {
    val GROUP = "no.nordicsemi.android"
    val POM_DESCRIPTION = "Nordic Android Common Libraries"
    val POM_URL = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins"
    val POM_SCM_URL = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins"
    val POM_SCM_CONNECTION = "scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git"
    val POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicSemiconductor/Android-Gradle-Plugins.git"
    val POM_LICENCE = "BSD-3-Clause"
    val POM_LICENCE_NAME = "The BSD 3-Clause License"
    val POM_LICENCE_URL = "http://opensource.org/licenses/BSD-3-Clause"
    val POM_DEVELOPER_ID = "syzi"
    val POM_DEVELOPER_NAME = "Sylwester Zielinski"
    val POM_DEVELOPER_EMAIL = "sylwester.zielinski@nordicsemi.no"
}
