package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.model.Movie
fun List<MovieModel>.asMovies(): List<Movie> = this.map { it.asMovie() }

fun MovieModel.asMovie() = Movie(
    id = id,
    title = title.toString(),
    adult = adult,
    overview = overview.toString(),
    releaseDate = releaseDate.toString(),
    genres = genres?.asGenres(),
    rating = rating,
    backdropPath = backdropPath,
    posterPath = posterPath,
    profilePath = profilePath,
    mediaType = mediaType,
    runtime = runtime
)

fun SearchModel.asMovie() = Movie(
    id = id,
    title = title.toString(),
    adult = adult,
    overview = overview.toString(),
    releaseDate = releaseDate.toString(),
    genres = genres?.asGenres(),
    rating = rating,
    backdropPath = backdropPath,
    posterPath = posterPath,
    profilePath = profilePath,
    mediaType = mediaType,
    runtime = runtime
)

fun Movie.asSearchModel() = SearchModel(
    id = id?:0,
    title = title.toString(),
    adult = adult,
    overview = overview.toString(),
    releaseDate = releaseDate.toString(),
    genres = genres?.asGenreModel(),
    rating = rating,
    backdropPath = backdropPath,
    posterPath = posterPath,
    profilePath = profilePath,
    mediaType = mediaType,
    runtime = runtime,
    timestamp = System.currentTimeMillis()
)

