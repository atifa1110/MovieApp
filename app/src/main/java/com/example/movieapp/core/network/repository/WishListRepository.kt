package com.example.movieapp.core.network.repository

import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface WishListRepository {
    fun getWishlist(): Flow<CinemaxResponse<List<WishlistModel>>>

    suspend fun addMovieToWishlist(movie : MovieDetailModel)
    suspend fun addTvShowToWishlist(tvShow : TvShowDetailModel)

    suspend fun removeMovieFromWishlist(id: Int)
    suspend fun removeTvShowFromWishlist(id: Int)

}