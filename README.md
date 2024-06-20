# Nordic Gradle Plugins for Android

The repo contains plugins which are shared between Nordic's Android applications.
[Now in Android app](https://github.com/android/nowinandroid) from Google has been an inspiration 
for writing Nordic's dedicated plugins.

## Plugins

List of plugins currently available in the repository.

### Android plugins

1. [nno.nordicsemi.android.plugin.application](plugins/src/main/kotlin/AndroidApplicationConventionPlugin.kt)
2. [nno.nordicsemi.android.plugin.application.compose](plugins/src/main/kotlin/AndroidApplicationComposeConventionPlugin.kt)
3. [nno.nordicsemi.android.plugin.library](plugins/src/main/kotlin/AndroidLibraryConventionPlugin.kt)
4. [nno.nordicsemi.android.plugin.library.compose](plugins/src/main/kotlin/AndroidLibraryComposeConventionPlugin.kt)
5. [nno.nordicsemi.android.plugin.feature](plugins/src/main/kotlin/AndroidFeatureConventionPlugin.kt)
6. [nno.nordicsemi.android.plugin.hilt](plugins/src/main/kotlin/AndroidHiltConventionPlugin.kt)
7. [nno.nordicsemi.android.plugin.kotlin](plugins/src/main/kotlin/AndroidNexusRepositoryPlugin.kt)
8. [nno.nordicsemi.android.plugin.nexus](plugins/src/main/kotlin/AndroidKotlinConventionPlugin.kt)

### JVM plugins

1. [no.nordicsemi.jvm.plugin.nexus](plugins/src/main/kotlin/JvmNexusRepositoryPlugin.kt)
2. [no.nordicsemi.jvm.plugin.kotlin](plugins/src/main/kotlin/JvmKotlinConventionPlugin.kt)

Plugins are released to Nexus repository and are available by their ids and version number.

## Version catalog

The repository also contains Gradle Version Catalog which consumes [toml](gradle/libs.versions.toml) file.
The file is automatically used by gradle to create libs reference in gradle.kts files because it 
is located in gradle directory. No additional set up is required.










