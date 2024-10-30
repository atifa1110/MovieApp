package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.data.asGenreEntity
import com.example.movieapp.core.data.asGenreModel
import com.example.movieapp.core.data.asGenreModels
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.domain.SearchModel

internal fun SearchEntity.asSearchModel() = SearchModel(
    id = id,
    title = title,
    overview = overview,
    popularity = popularity,
    releaseDate = releaseDate.toString(),
    adult = adult,
    genres = genreEntities?.asGenreModel(),
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath.toString(),
    backdropPath = backdropPath.toString(),
    video = video,
    rating = rating,
    mediaType = mediaType,
    runtime = runtime,
    timestamp = timestamp
)

internal fun SearchModel.asSearchEntity() = SearchEntity(
    id = id,
    title = title.toString(),
    overview = overview.toString(),
    popularity = popularity?:0.0,
    releaseDate = releaseDate,
    adult = adult?:false,
    genreEntities = genres?.asGenreEntity(),
    originalTitle = originalTitle.toString(),
    originalLanguage = originalLanguage.toString(),
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    posterPath = posterPath,
    backdropPath = backdropPath,
    video = video?:false,
    rating = rating?:0.0,
    mediaType = mediaType.toString(),
    runtime = runtime?:0,
    timestamp = timestamp?:0
)