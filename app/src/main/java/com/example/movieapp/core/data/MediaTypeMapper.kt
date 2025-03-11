package com.example.movieapp.core.data

import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.network.NetworkMediaType

fun MediaTypeModel.Movie.asMediaType() = MediaType.Movie[mediaType]

fun MediaTypeModel.TvShow.asMediaType() = MediaType.TvShow[mediaType]


fun MediaType.Movie.asNetworkMediaType() = NetworkMediaType.Movie[mediaType]
fun MediaType.TvShow.asNetworkMediaType() = NetworkMediaType.TvShow[mediaType]

fun MediaType.Wishlist.asMediaTypeModel() = when (this) {
    MediaType.Wishlist.Movie -> MediaTypeModel.Wishlist.Movie
    MediaType.Wishlist.TvShow -> MediaTypeModel.Wishlist.TvShow
}