package com.example.movieapp.core.network.response

sealed class CinemaxResponse<out T> {
    data class Success<out T>(val value: T) : CinemaxResponse<T>()
    data class Failure<out T>(val error: String, val data: T? = null) : CinemaxResponse<T>()
    object Loading : CinemaxResponse<Nothing>()

    companion object {
        fun <T> success(data: T): CinemaxResponse<T> = Success(data)
        fun <T> failure(error: String, data: T? = null): CinemaxResponse<T> = Failure(error, data)
        //fun <T> loading(data: T? = null): CinemaxResponse<T> = Loading()
    }
}
