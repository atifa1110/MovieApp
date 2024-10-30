package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.data.asGenreEntity
import com.example.movieapp.core.data.asGenreModel
import com.example.movieapp.core.data.asGenreModels
import com.example.movieapp.core.data.asMediaTypeModel
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.WishlistModel


internal fun WishlistEntity.asWishlistModel() = WishlistModel(
    id = networkId,
    mediaType = mediaType.asMediaTypeModel(),
    title = title,
    genre = genreEntities?.asGenreModel(),
    rating = rating,
    posterPath = posterPath,
    isWishListed = isWishListed
)

internal fun MediaType.Wishlist.asWishlistEntity(movie: MovieDetailModel) = WishlistEntity(
    mediaType = this,
    networkId = movie.id,
    title = movie.title,
    genreEntities = movie.genres.asGenreEntity(),
    rating = movie.rating,
    posterPath = movie.posterPath.toString(),
    isWishListed = movie.isWishListed
)