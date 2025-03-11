package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.asMediaType
import com.example.movieapp.core.network.getDateCustomFormat
import com.example.movieapp.core.network.getRating
import com.example.movieapp.core.network.getTitle
import com.example.movieapp.core.network.response.movies.MovieNetwork

internal fun MovieWithGenres.asMovieModel() = MovieModel(
    id = movie.networkId,
    title = movie.title,
    overview = movie.overview,
    popularity = movie.popularity,
    releaseDate = movie.releaseDate,
    adult = movie.adult,
    genres = emptyList(),
    originalTitle = movie.originalTitle,
    originalLanguage = movie.originalLanguage,
    voteAverage = movie.voteAverage,
    voteCount = movie.voteCount,
    posterPath = movie.posterPath,
    backdropPath = movie.backdropPath,
    video = movie.video,
    rating = movie.rating,
    mediaType = movie.mediaType.mediaType,
    runtime = movie.runtime
)

internal fun MovieEntity.asMovieModel() = MovieModel(
    id = networkId,
    title = title,
    overview = overview,
    popularity = popularity,
    releaseDate = releaseDate,
    adult = adult,
    genres = genreIds?.asGenreModels(),
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath,
    backdropPath = backdropPath,
    video = video,
    rating = rating,
    mediaType = mediaType.mediaType,
    runtime = runtime
)

internal fun MovieNetwork.asMovieModel(runtime : Int?) = MovieModel(
    id = id,
    title = getTitle(originalTitle, originalName),
    overview = overview,
    popularity = popularity,
    releaseDate = getDateCustomFormat(releaseDate,firstAirDate),
    adult = adult,
    genres = genreIds?.asGenreModels(),
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath?.asImageURL(),
    profilePath = profilePath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    video = video,
    rating = voteAverage?.getRating(),
    mediaType = mediaType?.asMediaType(),
    runtime = runtime?: NoMovieRuntimeValue
)

internal fun MovieNetwork.asMovieModel() = MovieModel(
    id = id,
    title = getTitle(originalTitle, originalName),
    overview = overview,
    popularity = popularity,
    releaseDate = getDateCustomFormat(releaseDate,firstAirDate),
    adult = adult,
    genres = genreIds?.asGenreModels(),
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath?.asImageURL(),
    profilePath = profilePath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    video = video,
    rating = voteAverage?.getRating(),
    mediaType = mediaType?.asMediaType()
)

internal fun MovieNetwork.asMovieEntity(mediaType: MediaType.Movie, runtime: Int?) = MovieEntity(
    mediaType = mediaType,
    networkId = id?:0,
    title = getTitle(originalTitle, originalName),
    overview = overview.toString(),
    popularity = popularity?:0.0,
    releaseDate = getDateCustomFormat(releaseDate,firstAirDate),
    adult = adult?:false,
    genreIds = genreIds,
    originalTitle = originalTitle.toString(),
    originalLanguage = originalLanguage.toString(),
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    posterPath = posterPath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    video = video?:false,
    rating = voteAverage?.getRating()?:0.0,
    runtime = runtime?: NoMovieRuntimeValue
)

internal fun MovieNetwork.asMovieEntity(mediaType: MediaType.Movie) = MovieEntity(
    mediaType = mediaType,
    networkId = id?:0,
    title = getTitle(originalTitle, originalName),
    overview = overview.toString(),
    popularity = popularity?:0.0,
    releaseDate = getDateCustomFormat(releaseDate,firstAirDate),
    adult = adult?:false,
    genreIds = genreIds,
    originalTitle = originalTitle.toString(),
    originalLanguage = originalLanguage.toString(),
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    posterPath = posterPath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    video = video?:false,
    rating = voteAverage?.getRating()?:0.0,
    runtime = NoMovieRuntimeValue
)

const val NoMovieRuntimeValue = 0