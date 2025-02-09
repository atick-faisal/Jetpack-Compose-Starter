# Module :firebase:analytics

This module handles crash reporting and analytics through Firebase Crashlytics. It provides a
centralized way to track app crashes and report exceptions.

## Features

- Crash Reporting
- Error Tracking
- Exception Handling
- Custom Error Reports

## Dependencies Graph

```mermaid
graph TD
    A[firebase:analytics] --> B[core:android]
    A --> C[firebase.bom]
    C --> D[firebase.crashlytics]
    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #FFA726, stroke: #333, stroke-width: 2px
    style D fill: #FFA726, stroke: #333, stroke-width: 2px
```

## Usage

```kotlin
dependencies {
    implementation(project(":firebase:analytics"))
}
```

### Error Reporting

```kotlin
class YourClass @Inject constructor(
    private val crashReporter: CrashReporter
) {
    fun handleError(error: Throwable) {
        crashReporter.reportException(error)
    }
}
```

The module integrates with the app's error handling system to automatically report uncaught
exceptions to Firebase Crashlytics.