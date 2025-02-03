/*
 * Copyright 2023 Atick Faisal
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

@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

plugins {
    alias(libs.plugins.jetpack.application)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.firebase)
}

android {
    // ... Application Version ...
    val majorUpdateVersion = 1
    val minorUpdateVersion = 0
    val patchVersion = 0

    val mVersionCode = majorUpdateVersion.times(10_000)
        .plus(minorUpdateVersion.times(100))
        .plus(patchVersion)

    val mVersionName = "$majorUpdateVersion.$minorUpdateVersion.$patchVersion"
    val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_a")
    val currentTime = LocalDateTime.now().format(formatter)

    defaultConfig {
        versionCode = mVersionCode
        versionName = mVersionName
        applicationId = "dev.atick.compose"
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            applicationVariants.all {
                outputs.all {
                    (this as BaseVariantOutputImpl).outputFileName =
                        rootProject.name.replace(" ", "_") + "_demo_" +
                                (buildType.name + "_v") +
                                (versionName + "_") +
                                "${currentTime}.apk"
                    println(outputFileName)
                }
            }
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                println(
                    "keystore.properties file not found. Using debug key. Read more here: " +
                            "https://github.com/atick-faisal/Jetpack-Compose-Starter#release",
                )
                signingConfigs.getByName("debug")

            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    namespace = "dev.atick.demo"
}

dependencies {
    implementation(project(":auth"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":settings"))
    implementation(project(":storage:firebase"))
    implementation(project(":storage:preferences"))
    implementation(project(":storage:room"))
    implementation(project(":sync"))

    // ... Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // ... Billing
    implementation(libs.play.billing.ktx)
}