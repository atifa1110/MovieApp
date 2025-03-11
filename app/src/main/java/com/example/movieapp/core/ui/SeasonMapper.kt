package com.example.movieapp.core.ui

import com.example.movieapp.core.database.model.tv.SeasonEntity
import com.example.movieapp.core.domain.SeasonModel
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.Season
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getFormatReleaseDate
import com.example.movieapp.core.network.response.tv.NetworkSeason

internal fun List<SeasonModel>.asSeasons() = map(SeasonModel::asSeason)

private fun SeasonModel.asSeason() = Season(
    airDate = airDate,
    episodeCount = episodeCount,
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    rating = rating
)

internal fun List<Season>.asSeasonModel() = map(Season::asSeasonModel)

private fun Season.asSeasonModel() = SeasonModel(
    airDate = airDate,
    episodeCount = episodeCount,
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    rating = rating
)
