/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
/**
 * A sealed class that represents the result of a resource operation.
 *
 * @param T The type of data.
 * @param data The data result of the operation.
 * @param error The error that occurred during the operation, if any.
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null,
) {
    /**
     * Represents a successful result with data.
     *
     * @param T The type of data.
     * @param data The data result of the operation.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents a loading state with optional data.
     *
     * @param T The type of data.
     * @param data The optional data result of the operation.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Represents an error state with optional data and an error.
     *
     * @param T The type of data.
     * @param data The optional data result of the operation.
     * @param error The error that occurred during the operation.
     */
    class Error<T>(data: T? = null, error: Throwable) : Resource<T>(data, error)
}

/**
 * Creates a network-bound resource flow that performs a query and fetches new data if necessary.
 *
 * @param ResultType The type of the query result.
 * @param RequestType The type of the fetched data.
 * @param query The query function that returns a flow of the current data.
 * @param fetch The suspend function that fetches new data.
 * @param saveFetchedResult The suspend function that saves the fetched result.
 * @param shouldFetch The predicate function that determines if fetching new data is necessary.
 * @return A flow emitting the resource state based on the query and fetch operations.
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchedResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
): Flow<Resource<ResultType>> = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchedResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(it, throwable) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}
