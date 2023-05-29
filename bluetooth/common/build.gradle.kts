plugins {
    id("dev.atick.library")
    id("dev.atick.dagger.hilt")
}

android {
    namespace = "dev.atick.bluetooth.common"
}

dependencies {
    implementation(project(":core:android"))
}