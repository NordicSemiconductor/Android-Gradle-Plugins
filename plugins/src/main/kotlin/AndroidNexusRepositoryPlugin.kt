import com.android.build.api.dsl.AndroidSourceFile
import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension

class AndroidNexusRepositoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("maven-publish")
                apply("signing")
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
                    from(components["release"])
                    if (!project.state.executed) {
                        project.afterEvaluate {
                            configureDescription(this@withType, nexusPluginExt, tasks.getByName("androidSourcesJar"))
                        }
                    } else {
                        configureDescription(this@withType, nexusPluginExt, tasks.getByName("androidSourcesJar"))
                    }
                }
            }

            val signing = extensions.getByType<SigningExtension>()
            project.extra.set("signing.keyId", System.getenv("GPG_SIGNING_KEY"))
            project.extra.set("signing.password", System.getenv("GPG_PASSWORD"))
            project.extra.set("signing.secretKeyRingFile", System.getenv("../sec.gpg"))
            signing.sign(publishing.publications)
        }
    }

    private fun configureDescription(
        publication: MavenPublication,
        nexusPluginExt: NexusRepositoryPluginExt,
        androidSourcesJar: Task
    ) {
        publication.artifactId = nexusPluginExt.POM_ARTIFACT_ID
        publication.groupId = nexusPluginExt.GROUP

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
