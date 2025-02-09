# Module :core:android

This module serves as the foundation for all Android-specific functionality in the project. It
provides essential utilities, extensions, and base dependencies that are used across the
application.

## Features

- Core Android Extensions and Utils
- Coroutine Support
- JSON Serialization
- Date/Time Utilities
- Logging Infrastructure
- Base Hilt Setup

## Dependencies Graph

```mermaid
graph TD
    A[core:android] --> B[androidx.core]
    A --> C[kotlinx.coroutines]
    A --> D[kotlinx.serialization]
    A --> E[kotlinx.datetime]
    A --> F[dagger.hilt]
    A --> G[timber]
    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #64B5F6, stroke: #333, stroke-width: 2px
    style D fill: #64B5F6, stroke: #333, stroke-width: 2px
    style E fill: #64B5F6, stroke: #333, stroke-width: 2px
    style F fill: #64B5F6, stroke: #333, stroke-width: 2px
    style G fill: #64B5F6, stroke: #333, stroke-width: 2px
```

## Usage

This module is typically used as a dependency in other modules that require Android functionality:

```kotlin
dependencies {
    implementation(project(":core:android"))
}
```

All dependencies in this module are exposed as `api` to make them available to dependent modules.