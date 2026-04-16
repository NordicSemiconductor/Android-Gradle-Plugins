package no.nordicsemi.android

import org.gradle.api.JavaVersion

object AppConst {
    const val MIN_SDK = 23
    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    val JAVA_SOURCE_VERSION = JavaVersion.VERSION_1_8
    val JAVA_TARGET_VERSION = JavaVersion.VERSION_17
}