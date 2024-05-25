import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val javaVersion = libs.findVersion("java").get().toString()
            val minSdkVersion = libs.findVersion("minSdk").get().toString().toInt()
            val targetSdkVersion = libs.findVersion("targetSdk").get().toString().toInt()
            val compileSdkVersion = libs.findVersion("compileSdk").get().toString().toInt()

            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.dokka")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = compileSdkVersion

                defaultConfig {
                    minSdk = minSdkVersion
                    targetSdk = targetSdkVersion
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
                    targetCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
                }

                with(extensions.getByType<KotlinAndroidProjectExtension>()) {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.fromTarget(javaVersion))
                        freeCompilerArgs.addAll(
                            "-opt-in=kotlin.RequiresOptIn",
                            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        )
                    }
                }

                buildFeatures {
                    compose = true
                    buildConfig = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}