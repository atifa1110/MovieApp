package com.example.movieapp.core.network.datasource

import android.util.Log
import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.network.api.TvShowApiService
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.ErrorResponse
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.network.response.tv.TvShowDetailNetwork
import com.example.movieapp.core.network.response.tv.TvShowResponse
import com.google.gson.Gson
import javax.inject.Inject

class TvShowNetworkDataSource @Inject constructor(
    private val tvShowApiService: TvShowApiService
) {
    private val gson = Gson()

    suspend fun getByMediaType(
        mediaType: NetworkMediaType.TvShow,
        page: Int = Constants.DEFAULT_PAGE
    ): CinemaxResponse<TvShowResponse> {
        return try {
            val response = when (mediaType) {
                NetworkMediaType.TvShow.DISCOVER -> tvShowApiService.getDiscoverTv(page)
                NetworkMediaType.TvShow.TOP_RATED -> tvShowApiService.getTopRatedTv(page)
                NetworkMediaType.TvShow.POPULAR -> tvShowApiService.getPopularTv(page)
                NetworkMediaType.TvShow.NOW_PLAYING -> tvShowApiService.getOnTheAirTv(page)
                NetworkMediaType.TvShow.TRENDING -> tvShowApiService.getTrendingTv(page)
            }

            val body = response.body()
            val errorResponse = response.errorBody()?.string()
            val error = errorResponse?.let {
                gson.fromJson(it, ErrorResponse::class.java)
            } ?: ErrorResponse(-1, "Unknown error", false)

            // Check if the response is successful
            if (response.isSuccessful) {
                if (body != null) {
                    CinemaxResponse.success(body)
                } else {
                    CinemaxResponse.failure(error.statusMessage)
                }
            } else {
                CinemaxResponse.failure(error.statusMessage)
            }

        } catch (e: Exception) {
            CinemaxResponse.failure(e.localizedMessage?:"Unexpected Error!")
        }
    }

    suspend fun getDetailTv(
        id: Int,
        appendToResponse: String = Constants.Fields.DETAILS_APPEND_TO_RESPONSE
    ): CinemaxResponse<TvShowDetailNetwork> {
        return try{
            val response = tvShowApiService.getDetailsById(id, appendToResponse)
            val body = response.body()
            val errorResponse = response.errorBody()?.string()
            val error = errorResponse?.let {
                gson.fromJson(it, ErrorResponse::class.java)
            } ?: ErrorResponse(-1, "Unknown error", false)

            Log.d("NetworkDataSource",body?.name.toString())
            // Check if the response is successful
            if (response.isSuccessful) {
                if (body != null) {
                    CinemaxResponse.success(body)
                } else {
                    CinemaxResponse.failure(error.statusMessage)
                }
            } else {
                CinemaxResponse.failure(error.statusMessage)
            }
        }catch (e: Exception){
            CinemaxResponse.failure(e.localizedMessage?:"Unexpected Error!")
        }
    }

}