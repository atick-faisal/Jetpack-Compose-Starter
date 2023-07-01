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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties
import java.io.FileInputStream

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    id("dev.atick.application")
    id("dev.atick.dagger.hilt")
    id("dev.atick.firebase")
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
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            applicationVariants.all {
                outputs.all {
                    (this as BaseVariantOutputImpl).outputFileName =
                        rootProject.name.replace(" ", "_") + "_" +
                            (buildType.name + "_v") +
                            (versionName + "_") +
                            "${currentTime}.apk"
                    println(outputFileName)
                }
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    namespace = "dev.atick.compose"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":network"))
    implementation(project(":storage:room"))
    implementation(project(":storage:preferences"))
    implementation(project(":bluetooth:classic"))
}