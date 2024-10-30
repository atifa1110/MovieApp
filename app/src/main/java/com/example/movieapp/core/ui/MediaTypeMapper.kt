package com.example.movieapp.core.ui

import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.domain.MediaTypeModel

fun MediaType.Movie.asMediaTypeModel() = MediaTypeModel.Movie[mediaType]
fun MediaType.TvShow.asMediaTypeModel() = MediaTypeModel.TvShow[mediaType]

fun MediaType.Common.asMovieMediaType() = when (this) {
    MediaType.Common.Upcoming -> MediaType.Movie.Upcoming
    MediaType.Common.TopRated -> MediaType.Movie.TopRated
    MediaType.Common.Popular -> MediaType.Movie.Popular
    MediaType.Common.NowPlaying -> MediaType.Movie.NowPlaying
    MediaType.Common.Discover -> MediaType.Movie.Discover
    MediaType.Common.Trending -> MediaType.Movie.Trending
}

fun MediaType.Common.asTvShowMediaType() = when (this) {
    MediaType.Common.Upcoming -> null
    MediaType.Common.TopRated -> MediaType.TvShow.TopRated
    MediaType.Common.Popular -> MediaType.TvShow.Popular
    MediaType.Common.NowPlaying -> MediaType.TvShow.NowPlaying
    MediaType.Common.Discover -> MediaType.TvShow.Discover
    MediaType.Common.Trending -> MediaType.TvShow.Trending
}