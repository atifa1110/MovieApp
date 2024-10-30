package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.model.WishList

internal fun WishlistModel.asWishlist() = WishList(
    id = id,
    mediaType = mediaType,
    genres = genre?.asGenres(),
    title = title,
    rating = rating,
    posterPath = posterPath,
    isWishListed = isWishListed
)