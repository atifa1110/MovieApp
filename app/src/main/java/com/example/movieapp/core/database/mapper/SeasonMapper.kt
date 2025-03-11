package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.database.model.tv.SeasonEntity
import com.example.movieapp.core.domain.SeasonModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getFormatReleaseDate
import com.example.movieapp.core.network.getPercentageRating
import com.example.movieapp.core.network.response.tv.NetworkSeason

internal fun NetworkSeason.asSeasonEntity() = SeasonEntity(
    airDate = airDate?.getFormatReleaseDate()?:"Coming Soon",
    episodeCount = episodeCount,
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath?.asImageURL()?:"",
    seasonNumber = seasonNumber,
    rating = voteAverage.getPercentageRating().toString()
)

internal fun SeasonEntity.asSeasonModel() = SeasonModel(
    airDate = airDate,
    episodeCount = episodeCount,
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    rating = rating
)