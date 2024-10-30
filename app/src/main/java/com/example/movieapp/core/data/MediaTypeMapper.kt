package com.example.movieapp.core.data

import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.network.NetworkMediaType

internal fun MediaTypeModel.Movie.asMediaType() = MediaType.Movie[mediaType]
internal fun MediaTypeModel.TvShow.asMediaType() = MediaType.TvShow[mediaType]

internal fun MediaType.Movie.asNetworkMediaType() = NetworkMediaType.Movie[mediaType]
internal fun MediaType.TvShow.asNetworkMediaType() = NetworkMediaType.TvShow[mediaType]

internal fun MediaType.Wishlist.asMediaTypeModel() = when (this) {
    MediaType.Wishlist.Movie -> MediaTypeModel.Wishlist.Movie
    MediaType.Wishlist.TvShow -> MediaTypeModel.Wishlist.TvShow
}