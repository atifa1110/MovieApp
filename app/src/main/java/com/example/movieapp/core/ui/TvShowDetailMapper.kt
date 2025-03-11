package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.model.TvShowDetails
import com.example.movieapp.core.network.asImageURL

fun TvShowDetailModel.asTvShowDetails() = TvShowDetails(
    id = id,
    name = name,
    adult = adult,
    backdropPath = backdropPath,
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    genres = genres.asGenres(),
    seasons = seasons.asSeasons(),
    homepage = homepage,
    inProduction = inProduction,
    languages = languages,
    lastAirDate = lastAirDate,
    numberOfEpisodes = numberOfEpisodes,
    numberOfSeasons = numberOfSeasons,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    originalName = originalName,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath?.asImageURL(),
    status = status,
    tagline = tagline,
    type = type,
    voteAverage = voteAverage,
    voteCount = voteCount,
    credits = credits.asCredits(),
    rating = rating,
    isWishListed = isWishListed
)
