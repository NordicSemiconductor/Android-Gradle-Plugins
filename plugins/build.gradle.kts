/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `version-catalog`
    `kotlin-dsl`
    `maven-publish`
    signing
    id("com.gradle.plugin-publish") version "1.1.0"
}
apply(from = "../gradle/git-tag-version.gradle")

val getVersionNameFromTags: groovy.lang.Closure<String> by ext

group = "no.nordicsemi.android.gradle"
version = getVersionNameFromTags()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

pluginBundle {
    website = "https://www.nordicsemi.com/"
    vcsUrl = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins"
    tags = listOf("nordicsemi", "Android")
}

gradlePlugin {
    plugins {
        register("application.compose") {
            id = "no.nordicsemi.android.gradle.application.compose"
            displayName = "Application with Compose"
            description = "Application plugin extension with Compose and Material3 dependencies. This plugin includes 'application' plugin."
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("application") {
            id = "no.nordicsemi.android.gradle.application"
            displayName = "Standalone Application configuration"
            description = "Application plugin extension"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("library.compose") {
            id = "no.nordicsemi.android.gradle.library.compose"
            displayName = "Library with Compose"
            description = "Library plugin extension with Compose and Material3 dependencies. This plugin extends 'library' plugin."
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("library") {
            id = "no.nordicsemi.android.gradle.library"
            displayName = "Standalone library configuration"
            description = "Library plugin extension"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("feature") {
            id = "no.nordicsemi.android.gradle.feature"
            displayName = "Feature plugin"
            description = "UI feature plugin with Hilt & Compose. This plugin extends 'library.compose' and 'hilt' plugins and adds Compose navigation."
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = "no.nordicsemi.android.gradle.hilt"
            displayName = "Hilt plugin"
            description = "Plugin enabling Hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("nexus") {
            id = "no.nordicsemi.android.gradle.nexus"
            displayName = "Nexus plugin"
            description = "Plugin creating a task for publishing to Nexus repository."
            implementationClass = "AndroidNexusRepositoryPlugin"
        }
    }
}

ext["signing.keyId"] = System.getenv("GPG_SIGNING_KEY")
ext["signing.password"] = System.getenv("GPG_PASSWORD")
ext["signing.secretKeyRingFile"] = "../sec.gpg"

signing {
    sign(publishing.publications)
}
