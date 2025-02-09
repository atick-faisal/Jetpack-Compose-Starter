# Module :core:ui

This module contains all the common UI components, theming, and design system elements used
throughout the application. It provides a consistent look and feel across all features.

## Features

- Material3 Design System
- Common UI Components
- Theme Configuration
- Navigation Setup
- Image Loading
- Animation Support

## Dependencies Graph

```mermaid
graph TD
    A[core:ui] --> B[core:android]
    A --> C[compose.bom]
    C --> D[compose.foundation]
    C --> E[compose.material3]
    C --> F[compose.navigation]
    A --> G[lifecycle]
    A --> H[coil]
    A --> I[lottie]

    subgraph "Compose Components"
        D
        E
        F
    end

    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #FF7043, stroke: #333, stroke-width: 2px
    style G fill: #64B5F6, stroke: #333, stroke-width: 2px
    style H fill: #64B5F6, stroke: #333, stroke-width: 2px
    style I fill: #64B5F6, stroke: #333, stroke-width: 2px
```

## Usage

This module is used by all feature modules that require UI components:

```kotlin
dependencies {
    implementation(project(":core:ui"))
}
```

### Key Components

- **Design System**: Common theme, typography, and color schemes
- **Reusable Components**: Common UI components like buttons, text fields, etc.
- **Navigation**: Type-safe navigation utilities
- **Image Loading**: Coil integration for efficient image loading
- **Animations**: Lottie integration for rich animations

All UI dependencies are exposed as `api` to ensure consistent versioning across the app.