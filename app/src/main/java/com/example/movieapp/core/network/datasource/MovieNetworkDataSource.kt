package com.example.movieapp.core.network.datasource

import android.util.Log
import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.network.api.MovieApiService
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.ErrorResponse
import com.example.movieapp.core.network.response.GenreResponse
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.google.gson.Gson
import javax.inject.Inject

class MovieNetworkDataSource @Inject constructor(
    private val movieService: MovieApiService
) {
    private val gson = Gson()

    suspend fun getGenreMovie() : CinemaxResponse<GenreResponse>{
        return try {
            val response = movieService.getGenreMovie()

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

    suspend fun getByMediaType(
        mediaType: NetworkMediaType.Movie,
        page: Int = Constants.DEFAULT_PAGE
    ): CinemaxResponse<MovieResponse> {
        return try {
            val response = when (mediaType) {
                NetworkMediaType.Movie.UPCOMING -> movieService.getUpcomingMovie(page)
                NetworkMediaType.Movie.TOP_RATED -> movieService.getTopRatedMovie(page)
                NetworkMediaType.Movie.POPULAR -> movieService.getPopularMovie(page)
                NetworkMediaType.Movie.NOW_PLAYING -> movieService.getNowPlayingMovie(page)
                NetworkMediaType.Movie.TRENDING -> movieService.getTrendingMovie(page)
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

    suspend fun search(
        query: String,
        page: Int = Constants.DEFAULT_PAGE
    ): CinemaxResponse<MovieResponse> {
        return try{
            val response = movieService.getSearchAll(query, page)
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
        }catch (e: Exception){
            CinemaxResponse.failure(e.localizedMessage?:"Unexpected Error!")
        }
    }

    suspend fun getDetailMovie(
        id: Int,
        appendToResponse: String = Constants.Fields.DETAILS_APPEND_TO_RESPONSE
    ): CinemaxResponse<MovieDetailNetwork> {
        return try{
            val response = movieService.getDetailsById(id, appendToResponse)
            val body = response.body()
            val errorResponse = response.errorBody()?.string()
            val error = errorResponse?.let {
                gson.fromJson(it, ErrorResponse::class.java)
            } ?: ErrorResponse(-1, "Unknown error", false)

            Log.d("NetworkDataSource",body?.title.toString())
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