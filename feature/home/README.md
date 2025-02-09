# Module :feature:home

Main feature module implementing the home screen with item list and CRUD operations.

## Features

- Item List Display
- Create/Edit Items
- Delete with Undo
- Offline Support
- Pull to Refresh
- Background Sync

## Dependencies Graph

```mermaid
graph TD
    A[feature:home] --> B[core:ui]
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

1. **Home Screen**: Shows the list of items

	```kotlin
	@Composable
	fun HomeRoute(
	    onItemClick: (String) -> Unit,
	    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean
	)
	```

2. **Item Screen**: Create/Edit item screen

	```kotlin
	@Composable
	fun ItemRoute(
	   onBackClick: () -> Unit,
	   onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean
	)
	```