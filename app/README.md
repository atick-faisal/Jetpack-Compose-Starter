# Module :app

Main application module that coordinates feature modules and handles app-level configuration.

## Dependencies Graph

```mermaid
graph TD
    A[app] --> B[core:ui]
    A --> C[core:network]
    A --> D[core:preferences]
    A --> E[feature:auth]
    A --> F[feature:home]
    A --> G[feature:profile]
    A --> H[feature:settings]
    A --> I[firebase:analytics]
    A --> J[sync]
    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #64B5F6, stroke: #333, stroke-width: 2px
    style D fill: #64B5F6, stroke: #333, stroke-width: 2px
    style E fill: #FF7043, stroke: #333, stroke-width: 2px
    style F fill: #FF7043, stroke: #333, stroke-width: 2px
    style G fill: #FF7043, stroke: #333, stroke-width: 2px
    style H fill: #FF7043, stroke: #333, stroke-width: 2px
    style I fill: #FFA726, stroke: #333, stroke-width: 2px
    style J fill: #7E57C2, stroke: #333, stroke-width: 2px
```

## Key Features

- Main application
- Feature coordination
- Analytics setup
- Splash screen
- Version management
- ProGuard rules

## Version Management

```kotlin
// ... Application Version ...
val majorUpdateVersion = 1
val minorUpdateVersion = 0
val patchVersion = 0

val mVersionCode = majorUpdateVersion.times(10_000)
    .plus(minorUpdateVersion.times(100))
    .plus(patchVersion)
```