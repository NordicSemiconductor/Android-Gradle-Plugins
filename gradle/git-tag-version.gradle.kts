fun calculateVersionCodeFromTags(): Int {
    return try {
        val result = providers.exec {
            commandLine("git", "tag", "--list")
            isIgnoreExitValue = true
        }
        val listOfTags = result.standardOutput.asText.get()
        2 + listOfTags.split("\n").size
    } catch (ignored: Exception) {
        -1
    }
}

fun calculateVersionNameFromTags(): String? {
    return try {
        val result = providers.exec {
            commandLine("git", "describe", "--tags", "--abbrev=0")
            isIgnoreExitValue = true
        }
        if (result.result.get().exitValue != 0) return null
        val version = result.standardOutput.asText.get().trim()
        // Version may have % to add additional information
        version.split("%")[0]
    } catch (ignored: Exception) {
        ignored.printStackTrace()
        null
    }
}

extra["versionCodeFromTags"] = calculateVersionCodeFromTags() ?: "local"
extra["versionNameFromTags"] = calculateVersionNameFromTags() ?: "local"
