package com.example.movieapp.core.ui

import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.domain.MediaTypeModel

fun MediaType.Movie.asMediaTypeModel() = MediaTypeModel.Movie[mediaType]
fun MediaType.TvShow.asMediaTypeModel() = MediaTypeModel.TvShow[mediaType]

fun MediaType.Common.asMovieMediaType() = when (this) {
    MediaType.Common.Movie.Upcoming -> MediaType.Movie.Upcoming
    MediaType.Common.Movie.Popular -> MediaType.Movie.Popular
    MediaType.Common.Movie.NowPlaying -> MediaType.Movie.NowPlaying
    MediaType.Common.Movie.Trending -> MediaType.Movie.Trending
    else -> MediaType.Movie.Upcoming
}

fun MediaType.Common.asTvShowMediaType() = when (this) {
    MediaType.Common.TvShow.Popular -> MediaType.TvShow.Popular
    MediaType.Common.TvShow.NowPlaying -> MediaType.TvShow.NowPlaying
    MediaType.Common.TvShow.Trending -> MediaType.TvShow.Trending
    else -> MediaType.TvShow.TopRated
}