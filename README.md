# Nordic Gradle Plugins for Android

The repo contains plugins which are shared between Nordic's Android applications.
[Now in Android app](https://github.com/android/nowinandroid) from Google has been an inspiration for writing Nordic's dedicated plugins.

## Plugins

List of plugins currently available in the repository:

1. [no.nordicsemi.android.application](plugins/src/main/kotlin/AndroidApplicationConventionPlugin.kt)
2. [no.nordicsemi.android.application.compose](plugins/src/main/kotlin/AndroidApplicationComposeConventionPlugin.kt)
3. [no.nordicsemi.android.library](plugins/src/main/kotlin/AndroidLibraryConventionPlugin.kt)
4. [no.nordicsemi.android.library.compose](plugins/src/main/kotlin/AndroidLibraryComposeConventionPlugin.kt)
5. [no.nordicsemi.android.feature](plugins/src/main/kotlin/AndroidFeatureConventionPlugin.kt)
6. [no.nordicsemi.android.kotlin](plugins/src/main/kotlin/AndroidKotlinConventionPlugin.kt)
6. [no.nordicsemi.android.hilt](plugins/src/main/kotlin/AndroidHiltConventionPlugin.kt)
6. [no.nordicsemi.android.gradle.nexus](plugins/src/main/kotlin/AndroidNexusRepositoryPlugin.kt)

Plugins are released to Nexus repository and are available by their ids and version number.

## Version catalog
The repository also contains Gradle Version Catalog which consumes [toml](gradle/libs.versions.toml) file.
The file is automatically used by gradle to create libs reference in gradle.kts files because it is located in gradle directory.
No additional set up is required.
