package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.wishlist.WishlistDao
import com.example.movieapp.core.database.mapper.asWishlistEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import javax.inject.Inject

class WishlistDatabaseSource @Inject constructor(
    private val wishlistDao : WishlistDao
) {
    fun getWishlist() = wishlistDao.getByMediaType()

    suspend fun addMovieToWishlist(movie: MovieDetailModel) =
        wishlistDao.insert(MediaType.Wishlist.Movie.asWishlistEntity(movie))

    suspend fun addTvShowToWishlist(tvShow : TvShowDetailModel) =
        wishlistDao.insert(MediaType.Wishlist.TvShow.asWishlistEntity(tvShow))

    suspend fun removeMovieFromWishlist(id: Int) =
        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.Movie, id)

    suspend fun removeTvShowFromWishlist(id: Int) =
        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.TvShow, id)

    suspend fun isMovieWishListed(id: Int) = wishlistDao.isWishListed(MediaType.Wishlist.Movie, id)
    suspend fun isTvShowWishListed(id: Int) = wishlistDao.isWishListed(MediaType.Wishlist.TvShow, id)
}