package com.example.movieapp.core.network.repository

import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface WishListRepository {
    fun getMovies(): Flow<CinemaxResponse<List<WishlistModel>>>
    suspend fun addMovieToWishlist(movie : MovieDetailModel)
    suspend fun removeMovieFromWishlist(id: Int)
}