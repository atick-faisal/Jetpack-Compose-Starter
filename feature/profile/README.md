# Module :feature:profile

User profile management module with account details and preferences.

## Features

- Profile Information Display
- Avatar Management
- Account Settings
- User Stats
- Sign Out Flow

## Dependencies Graph

```mermaid
graph TD
    A[feature:profile] --> B[core:ui]
    A --> C[data]
    B --> D[core:android]

    subgraph "Core Dependencies"
        B
        C
        D
    end

    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #64B5F6, stroke: #333, stroke-width: 2px
    style D fill: #64B5F6, stroke: #333, stroke-width: 2px
```

## Key Components

1. **Profile Screen**: Shows the user profile information

	```kotlin
	@Composable
	fun ProfileRoute(
	  onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean
	)
	```