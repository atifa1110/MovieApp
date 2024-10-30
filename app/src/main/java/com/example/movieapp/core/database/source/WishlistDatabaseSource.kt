package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.WishlistDao
import com.example.movieapp.core.database.mapper.asWishlistEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MovieDetailModel
import javax.inject.Inject

class WishlistDatabaseSource @Inject constructor(
    private val wishlistDao : WishlistDao
) {
    fun getMovies() = wishlistDao.getByMediaType(MediaType.Wishlist.Movie)
    fun getTvShows() = wishlistDao.getByMediaType(MediaType.Wishlist.TvShow)

    suspend fun addMovieToWishlist(movie: MovieDetailModel) =
        wishlistDao.insert(MediaType.Wishlist.Movie.asWishlistEntity(movie))

    suspend fun addTvShowToWishlist(movie: MovieDetailModel) =
        wishlistDao.insert(MediaType.Wishlist.TvShow.asWishlistEntity(movie))

    suspend fun removeMovieFromWishlist(id: Int) =
        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.Movie, id)

    suspend fun removeTvShowFromWishlist(id: Int) =
        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.TvShow, id)

    suspend fun isMovieWishListed(id: Int) = wishlistDao.isWishListed(MediaType.Wishlist.Movie, id)
    suspend fun isTvShowWishListed(id: Int) = wishlistDao.isWishListed(MediaType.Wishlist.TvShow, id)
}