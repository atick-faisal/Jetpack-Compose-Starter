# Module :data

Central data management module that coordinates between local and remote data sources.

## Dependencies Graph

```mermaid
graph TD
    A[data] --> B[core:android]
    A --> C[core:network]
    A --> D[core:preferences]
    A --> E[core:room]
    A --> F[firebase:auth]
    A --> G[firebase:firestore]

    subgraph "Core"
        B
        C
        D
        E
    end

    subgraph "Firebase"
        F
        G
    end

    style A fill: #4CAF50, stroke: #333, stroke-width: 2px
    style B fill: #64B5F6, stroke: #333, stroke-width: 2px
    style C fill: #64B5F6, stroke: #333, stroke-width: 2px
    style D fill: #64B5F6, stroke: #333, stroke-width: 2px
    style E fill: #64B5F6, stroke: #333, stroke-width: 2px
    style F fill: #FFA726, stroke: #333, stroke-width: 2px
    style G fill: #FFA726, stroke: #333, stroke-width: 2px
```

## Key Components

- Repository implementations
- Data models and mappers
- Offline-first logic
- Synchronization handling
- Error management

## Implementation Pattern

```kotlin
interface YourRepository {
    fun getData(): Flow<List<Data>>
}

class YourRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : YourRepository {
    override fun getData(): Flow<List<Data>> =
        networkBoundResource(
            query = { localDataSource.getData() },
            fetch = { remoteDataSource.getData() },
            saveFetchResult = { localDataSource.saveData(it) }
        )
}
```