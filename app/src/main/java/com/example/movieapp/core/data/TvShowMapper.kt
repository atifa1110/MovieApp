package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getRating
import com.example.movieapp.core.network.response.tv.TvShowNetwork

internal fun TvShowEntity.asTvShowModel() = TvShowModel(
    id = networkId,
    name = name,
    overview = overview,
    popularity = popularity,
    firstAirDate = firstAirDate,
    genres = genres.asGenreModels(),
    originalName = originalName,
    originalLanguage = originalLanguage,
    originCountry = originCountry,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating
)

internal fun TvShowNetwork.asTvShowEntity(mediaType: MediaType.TvShow) = TvShowEntity(
    mediaType = mediaType,
    networkId = id,
    name = name?:"",
    overview = overview?:"",
    popularity = popularity?:0.0,
    firstAirDate = firstAirDate?:"",
    genres = genreIds?.asGenres()?: emptyList(),
    originalName = originalName?:"",
    originalLanguage = originalLanguage?:"",
    originCountry = originCountry?: emptyList(),
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    posterPath = posterPath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    runtime = 0,
    rating = voteAverage?.getRating()?:0.0,
)

internal fun TvShowNetwork.asTvShowEntity(mediaType: MediaType.TvShow, runtime: Int?) = TvShowEntity(
    mediaType = mediaType,
    networkId = id,
    name = name?:"",
    overview = overview?:"",
    popularity = popularity?:0.0,
    firstAirDate = firstAirDate?:"",
    genres = genreIds?.asGenres()?: emptyList(),
    originalName = originalName?:"",
    originalLanguage = originalLanguage?:"",
    originCountry = originCountry?: emptyList(),
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    posterPath = posterPath?.asImageURL(),
    backdropPath = backdropPath?.asImageURL(),
    runtime = runtime?:0,
    rating = voteAverage?.getRating()?:0.0,
)