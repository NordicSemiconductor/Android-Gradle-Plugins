import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

class KotlinOptionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<KotlinBasePlugin> {
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? = target.findProperty("warningsAsErrors") as? String
            when (this) {
                is KotlinAndroidProjectExtension -> compilerOptions
                is KotlinJvmProjectExtension -> compilerOptions
                is KotlinMultiplatformExtension -> compilerOptions
                else -> TODO("Unsupported project extension $this ${this::class}")
            }.apply {
                allWarningsAsErrors.set(warningsAsErrors.toBoolean())

                languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3)
                apiVersion.set(KotlinVersion.KOTLIN_2_3)
                optIn.add("kotlin.RequiresOptIn")
                optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn.add("kotlinx.coroutines.FlowPreview")
                // https://kotlinlang.org/docs/whatsnew23.html#explicit-backing-fields
                // freeCompilerArgs.add("-Xexplicit-backing-fields") // -> https://github.com/NordicSemiconductor/Nordic-Gradle-Plugins/issues/372
                // https://kotlinlang.org/docs/whatsnew23.html#unused-return-value-checker
                freeCompilerArgs.add("-Xreturn-value-checker=full")
            }
        }
    }
}