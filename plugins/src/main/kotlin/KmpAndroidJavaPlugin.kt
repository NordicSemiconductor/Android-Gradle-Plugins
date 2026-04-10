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

import com.android.build.api.dsl.CommonExtension
import no.nordicsemi.android.AppConst
import no.nordicsemi.asJvmTarget
import no.nordicsemi.hasAndroidPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpAndroidJavaPlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        target.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            target.extensions.configure<KotlinMultiplatformExtension> {
                targets
                    .filterIsInstance<HasConfigurableKotlinCompilerOptions<*>>()
                    .mapNotNull { target -> target.compilerOptions as? KotlinJvmCompilerOptions }
                    .onEach { androidTarget ->
                        androidTarget.jvmTarget.set(AppConst.JAVA_TARGET_VERSION.asJvmTarget())
                    }
            }

            target.extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions.jvmTarget.set(AppConst.JAVA_TARGET_VERSION.asJvmTarget())
            }
        }
    }

    private fun configureAndroidPlugin(target: Project) {
        if (!target.hasAndroidPlugin) return
        target.pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
            target.extensions.configure<CommonExtension> {
                compileOptions.sourceCompatibility = AppConst.JAVA_SOURCE_VERSION
                compileOptions.targetCompatibility = AppConst.JAVA_TARGET_VERSION
            }
        }
    }

    override fun apply(target: Project) {
        configureAndroidPlugin(target)
        configureAndroidKmpPlugin(target)
    }
}