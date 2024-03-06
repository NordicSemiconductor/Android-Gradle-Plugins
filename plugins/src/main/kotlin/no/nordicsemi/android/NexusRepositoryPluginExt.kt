package no.nordicsemi.android

import org.gradle.api.publish.maven.MavenPom

abstract class NexusRepositoryPluginExt {
    // Required parameters:
    lateinit var POM_ARTIFACT_ID: String
    lateinit var POM_NAME: String
    lateinit var POM_DESCRIPTION: String
    lateinit var POM_URL: String
    lateinit var POM_SCM_URL: String
    lateinit var POM_SCM_CONNECTION: String
    lateinit var POM_SCM_DEV_CONNECTION: String

    // Default values:
    var POM_GROUP: String? = null // If not set, the project's "group" will be used.

    // License
    var POM_LICENCE: String = "BSD-3-Clause"
    var POM_LICENCE_URL: String = "http://opensource.org/licenses/BSD-3-Clause"

    // Developer
    var POM_DEVELOPER_ID: String = "mag"
    var POM_DEVELOPER_NAME: String = "Mobile Applications Group"
    var POM_DEVELOPER_EMAIL: String = "mag@nordicsemi.no"
    var POM_ORG: String = "Nordic Semiconductor ASA"
    var POM_ORG_URL: String = "https://www.nordicsemi.com"
}
internal fun MavenPom.from(
    nexusPluginExt: NexusRepositoryPluginExt,
) = with (nexusPluginExt) {
    name.set(POM_NAME)
    description.set(POM_DESCRIPTION)
    url.set(POM_URL)

    // https://maven.apache.org/pom.html#licenses
    licenses {
        license {
            name.set(POM_LICENCE)
            url.set(POM_LICENCE_URL)
            // The two stated methods are repo (they may be downloaded from a Maven repository) or manual (they must be manually installed).
            distribution.set("repo")
        }
    }

    // https://maven.apache.org/pom.html#scm
    scm {
        url.set(POM_SCM_URL)
        connection.set(POM_SCM_CONNECTION)
        developerConnection.set(POM_SCM_DEV_CONNECTION)
    }

    // https://maven.apache.org/pom.html#organization
    organization {
        name.set(POM_ORG)
        url.set(POM_ORG_URL)
    }

    // https://maven.apache.org/pom.html#developers
    developers {
        developer {
            id.set(POM_DEVELOPER_ID)
            name.set(POM_DEVELOPER_NAME)
            email.set(POM_DEVELOPER_EMAIL)
            organization.set(POM_ORG)
            organizationUrl.set(POM_ORG_URL)
        }
    }
}