package no.nordicsemi.android.buildlogic

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.getVersionCodeFromTags(): Int {
    return try {
        val code = ByteArrayOutputStream()
        exec {
            commandLine("git", "tag", "--list")
            standardOutput = code
        }
        2 + code.toString().split("\n").size
    } catch (e: Exception) {
        -1
    }
}

fun Project.getVersionNameFromTags(): String? {
    return try {
        val code = ByteArrayOutputStream()
        exec {
            commandLine("git", "describe", "--tags", "--abbrev=0")
            standardOutput = code
        }
        code.toString().trim().split("%")[0]
    } catch (e: Exception) {
        null
    }
}
