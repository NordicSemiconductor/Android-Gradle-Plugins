import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidNordicCommonThemeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val nordicTheme = extensions.create("nordicNexusPublishing", NordicThemePluginExt::class.java)
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            afterEvaluate {
                if (nordicTheme.shouldIncludeNordicTheme) {
                    dependencies {
                        // Add Nordic Theme.
                        add("implementation", libs.findLibrary("nordic.theme").get())
                    }
                }

            }
        }

    }
}

abstract class NordicThemePluginExt {
    val shouldIncludeNordicTheme: Boolean = false
}