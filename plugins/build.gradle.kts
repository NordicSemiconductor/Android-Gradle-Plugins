/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

//pluginBundle {
//    website = "https://www.nordicsemi.com/"
//    vcsUrl = "https://github.com/NordicSemiconductor/Android-Gradle-Plugins"
//    tags = listOf("nordicsemi", "Android")
//}

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
        register("kotlin") {
            id = "no.nordicsemi.android.gradle.kotlin"
            displayName = "Kotlin plugin"
            description = "Plugin enabling Kotlin"
            implementationClass = "AndroidKotlinConventionPlugin"
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
