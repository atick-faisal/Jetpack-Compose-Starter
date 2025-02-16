# Performance Optimizations

This guide covers various performance optimization features available in the project.

## Compose Compiler Metrics

The project includes built-in support for Compose compiler metrics to help optimize your
composables.

Check the `gradle.properties` file to ensure the following:

```properties
enableComposeCompilerMetrics=true
enableComposeCompilerReports=true
```

This configuration will generate detailed reports in:

- `build/<module>/compose-metrics`: Composition metrics
- `build/<module>/compose-reports`: Compiler reports

### Using the Reports

1. **Skippability Report**: Shows which composables can't skip recomposition
2. **Events Report**: Details composition events
3. **Metrics Report**: Performance metrics for each composable

### Optimizing Composables

Use the reports to identify and fix recomposition issues:

```kotlin
// Bad: Class not marked as stable
data class UiState(
    val mutableList: MutableList<String> // Unstable type
)

// Good: Stable class with immutable properties
@Immutable
data class UiState(
    val list: List<String> // Immutable type
)
```

Common optimizations:

```kotlin
// Mark data classes as stable
@Stable
data class YourData(...)

// Use immutable collections
val items: ImmutableList<Item>

// Remember expensive computations
val filteredList = remember(items) {
    items.filter { ... }
}
```

> [!TIP]
> Use `@Immutable` for classes that never change and `@Stable` for classes whose properties may
> change but maintain identity.

## R8 and ProGuard Optimization

### Project Structure for Obfuscation

The project follows a consistent pattern for data models to simplify ProGuard/R8 rules:

```
├── feature/
│   └── your-feature/
│       └── model/  // Models kept unobfuscated
└── core/
    └── network/
        └── model/  // Data models kept unobfuscated
```

### ProGuard and Consumer Rules

If your app works in `debug` build but not in `release` build that typically indicates obfuscation
issues. In that case you need to add or edit the proguard rules. These can be found in <module>
/proguard-rules.pro for <module>/consumer-rules.pro files. For example:

```proguard
# Keep all models
-keep class **.model.** { *; }

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
```

> [!NOTE]
> The project's model organization makes it easy to keep data models unobfuscated while allowing
> safe obfuscation of implementation classes.

## Gradle Build Optimization

### Build Scan

The project includes Gradle Enterprise build scan support:

```kotlin
// settings.gradle.kts
plugins {
    id("com.gradle.develocity") version ("3.19.1")
}

develocity {
    buildScan {
        publishing.onlyIf { !System.getenv("CI").isNullOrEmpty() }
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}
```

Use build scans to:

- Identify slow tasks
- Find configuration issues
- Optimize dependency resolution

### Multi-Module Build Optimization

Take advantage of the project's modular structure:

1. **Make Module**: Instead of rebuilding the entire project, use Make Module:
	- Android Studio: Right-click module → Make Module
	- Command Line: `./gradlew :module:name:assembleDebug`

2. **Parallel Execution**: Enabled in `gradle.properties`:
	```properties
	org.gradle.parallel=true
	```

3. **Configuration Caching**: Already enabled for supported tasks

> [!TIP]
> When working on a feature, use Make Module on just that feature's module to significantly reduce
> build time.

## Baseline Profiles

> [!NOTE]
> Baseline profile support is coming soon to improve app startup performance.

### Planned Implementation

```kotlin
// build.gradle.kts
androidApplication {
    baselineProfile {
        automaticGenerationDuringBuild = true
    }
}
```

This will include:

- Startup trace collection
- Critical user path optimization
- Ahead-of-time compilation for key paths

## Firebase Performance Monitoring

This template includes Firebase Performance Monitoring setup through the Firebase convention plugin:

```kotlin
// FirebaseConventionPlugin.kt
class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.firebase.firebase-perf")
            }

            dependencies {
                "implementation"(libs.findLibrary("firebase.perf").get())
            }
        }
    }
}
```

This gives you automatic monitoring of:

- Network requests
- App startup time
- Screen render time
- Frozen frames

You can also add custom traces:

```kotlin
FirebasePerformance.getInstance().newTrace("custom_operation").apply {
    start()
    // Your code here
    stop()
}
```

> [!TIP]
> Use the Firebase Console to view performance metrics and identify bottlenecks in your app.

## Additional Performance Tips

1. **Image Loading**:

	```kotlin
	// Use Coil's memory cache
	ImageLoader(context) {
	   memoryCachePolicy(CachePolicy.ENABLED)
	   diskCachePolicy(CachePolicy.ENABLED)
	}
	```

2. **List Performance**:

	```kotlin
	// Use LazyColumn with keys
	LazyColumn {
	   items(
	       items = items,
	       key = { it.id } // Stable key for better recomposition
	   ) { item ->
	       // Item content
	   }
	}
	```

3. **State Hoisting**:

	```kotlin
	// Hoist state for better recomposition control
	@Composable
	fun StatefulComponent(
	   state: State, // Hoisted state
	   onStateChange: (State) -> Unit
	) {}
	```

4. **Navigation Performance**:

	```kotlin
	// Use NavOptions to control back stack. In JetpackAppState.kt:
	navController.navigate(route) {
	   launchSingleTop = true
	   restoreState = true
	}
	```

> [!IMPORTANT]
> Always profile your app's performance using Android Studio's CPU Profiler and Layout Inspector
> before and after optimizations to ensure they're effective.

## Further Reading
- [Useful Tips & Tricks](tips.md): Get useful tips for development and debugging
- [CI/CD Setup](github.md): Set up continuous integration and deployment for the project
- [Publishing to Play Store](fastlane.md): Learn how to publish your app to the Google Play Store
