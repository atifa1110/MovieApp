package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.MovieDetails

fun MovieDetails.asMovieDetailModel() = MovieDetailModel(
    id = id,
    title = title,
    overview = overview,
    backdropPath = backdropPath,
    budget = budget,
    genres = genres.asGenreModel(),
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    rating = rating,
    credits = CreditsModel(emptyList(), emptyList()),
    images = ImagesModel(emptyList(), emptyList()),
    videos = VideosModel(emptyList()),
    isWishListed = isWishListed,
    adult = false,
    homepage = "",
    imdbId = "",
    originalLanguage = "",
    originalTitle = "",
    popularity = 0.0,
    revenue = 0,
    status = "",
    tagline = ""
)

fun MovieDetailModel.asMovieDetails() = MovieDetails(
    id = id,
    title = title,
    overview = overview,
    backdropPath = backdropPath,
    budget = budget,
    genres = genres.asGenres(),
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime ?: NoMovieRuntimeValue,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    rating = rating,
    credits = credits.asCredits(),
    images = images?.asImages(),
    videos = videos?.asVideos(),
    isWishListed = isWishListed
)

const val NoMovieRuntimeValue = 0