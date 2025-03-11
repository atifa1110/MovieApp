package com.example.movieapp.core.network.repository

import com.example.movieapp.core.data.listMap
import com.example.movieapp.core.database.mapper.asWishlistModel
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WishListRepositoryImpl @Inject constructor(
    private val databaseSource: WishlistDatabaseSource
): WishListRepository {

    override fun getWishlist(): Flow<CinemaxResponse<List<WishlistModel>>> {
        return flow {
            try {
                emit(CinemaxResponse.Loading)
                val result = databaseSource.getWishlist().listMap(WishlistEntity::asWishlistModel).first()
                emit(CinemaxResponse.Success(result))
            } catch (e: Exception) {
                emit(CinemaxResponse.Failure(e.localizedMessage?: "Unexpected Error"))
            }
        }.catch { e ->
            emit(CinemaxResponse.Failure(e.localizedMessage?: "Unexpected Error"))
        }
    }


    override suspend fun addMovieToWishlist(movie: MovieDetailModel) {
        databaseSource.addMovieToWishlist(movie)
    }

    override suspend fun addTvShowToWishlist(tvShow: TvShowDetailModel) {
        databaseSource.addTvShowToWishlist(tvShow)
    }

    override suspend fun removeMovieFromWishlist(id: Int) {
        databaseSource.removeMovieFromWishlist(id)
    }

    override suspend fun removeTvShowFromWishlist(id: Int) {
        databaseSource.removeTvShowFromWishlist(id)
    }
}