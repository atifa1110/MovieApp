package com.example.movieapp.utils

import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> CinemaxResponse<RequestType>,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<CinemaxResponse<ResultType>> = flow {
    emit(CinemaxResponse.Loading)
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(CinemaxResponse.Loading)

        when (val response = fetch()) {
            is CinemaxResponse.Success -> {
                saveFetchResult(response.value)
                query().map { CinemaxResponse.success(it) }
            }
            is CinemaxResponse.Failure -> {
                query().map { CinemaxResponse.failure(response.error, it) }
            }
            else -> error("Unhandled state: $response")
        }
    } else {
        query().map { CinemaxResponse.success(it) }
    }

    emitAll(flow)
}
