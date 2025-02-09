import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.net.URI

internal fun Project.applyDokka() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    with(pluginManager) {
        apply("org.jetbrains.dokka")
    }

    extensions.configure<DokkaExtension> {
        moduleName.set(path)
        dokkaSourceSets.configureEach {
            includes.from("README.md")
            documentedVisibilities(
                VisibilityModifier.Public,
                VisibilityModifier.Private,
                VisibilityModifier.Protected,
                VisibilityModifier.Internal,
                VisibilityModifier.Package,
            )
            reportUndocumented.set(false)
            skipEmptyPackages.set(true)
            skipDeprecated.set(false)
            suppressGeneratedFiles.set(true)
            sourceLink {
                localDirectory.set(rootDir)
                remoteUrl.set(URI("https://github.com/atick-faisal/Jetpack-Compose-Starter/tree/main"))
                remoteLineSuffix.set("#L")
            }
            perPackageOption {
                matchingRegex.set(".*hilt_aggregated_deps.*")
                suppress.set(true)
            }
        }
        /* TODO: Setup footer
        pluginsConfiguration.withType<DokkaHtmlPluginParameters> {
            footerMessage.set("Footer Message")
        }
        */
    }

    dependencies {
        "dokkaPlugin"(libs.findLibrary("dokka.android.plugin").get())
        "dokkaPlugin"(libs.findLibrary("dokka.mermaid.plugin").get())
    }
}