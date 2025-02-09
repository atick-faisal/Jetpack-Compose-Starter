# Module :core:network

This module handles all network-related operations, including API communication, image loading, and
network state monitoring. It provides a centralized way to manage network requests and responses.

## Features

- REST API Communication (Retrofit)
- Network Interceptors (OkHttp)
- Image Loading (Coil)
- Network State Monitoring
- Secrets Management

## Dependencies Graph

```mermaid
graph TD
    A[core:network] --> B[core:android]
    A --> C[okhttp.bom]
    C --> D[okhttp.core]
    C --> E[okhttp.logging]
    A --> F[retrofit.core]
    F --> G[retrofit.kotlinx.serialization]
    A --> H[coil]
    H --> I[coil.svg]

    subgraph "Network Stack"
        C
        F
        H
    end

    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #FF7043, stroke: #333, stroke-width: 2px
    style F fill: #FF7043, stroke: #333, stroke-width: 2px
    style H fill: #FF7043, stroke: #333, stroke-width: 2px
```

## Usage

```kotlin
dependencies {
    implementation(project(":core:network"))
}
```

### Secret Management

Uses Gradle secrets plugin to manage API keys and endpoints:

```properties
# secrets.defaults.properties (version controlled)
apiKey=dummy-key
apiEndpoint=https://example.com
# local.properties (not version controlled)
apiKey=actual-secret-key
apiEndpoint=actual-endpoint
```
