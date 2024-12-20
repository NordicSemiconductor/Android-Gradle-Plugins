import no.nordicsemi.android.buildlogic.getVersionNameFromTags
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import java.util.Calendar
import org.jetbrains.kotlin.konan.file.createTempDir
import org.jetbrains.kotlin.konan.file.File

class NordicDokkaPlugin : Plugin<Project> {
    private val org = "Nordic Semiconductor ASA"

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.dokka")
            }

            val dokkaExtension = extensions.getByType<DokkaExtension>()
            dokkaExtension.apply {
                // Set the version.
                moduleVersion.set(getVersionNameFromTags())
                // Set the output directory for the documentation.
                // GitHub Pages are using "docs" directory.
                basePublicationsDirectory.set(rootDir.resolve("docs"))

                val icon = getResourceAsFile("logo-icon.svg")
                val styles = getResourceAsFile("logo-styles.css")

                // Set the footer message.
                pluginsConfiguration.named("html", DokkaHtmlPluginParameters::class.java) {
                    val year = Calendar.getInstance().get(Calendar.YEAR)
                    footerMessage.set("Copyright © 2022 - $year $org. All Rights Reserved.")
                    customAssets.from(icon.absolutePath)
                    customStyleSheets.from(styles.absolutePath)
                }
            }
        }
    }

    private fun getResourceAsFile(resourceName: String): File {
        val resource = this::class.java.getResourceAsStream("dokka/$resourceName")
            ?: throw IllegalStateException("Resource not found: $resourceName")
        val dir = createTempDir("dokka")
        val tempFile = File(dir, resourceName)
        dir.deleteOnExit()

        resource.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

}