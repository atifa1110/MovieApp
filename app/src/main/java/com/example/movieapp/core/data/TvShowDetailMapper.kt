package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getDateCustomFormat
import com.example.movieapp.core.network.getRating
import com.example.movieapp.core.network.response.tv.TvShowDetailNetwork

fun TvShowDetailNetwork.asTvShowDetailsEntity() = TvShowDetailsEntity(
    id = id,
    name = name,
    adult = adult,
    backdropPath = backdropPath,
    episodeRunTime = episodeRunTime?: listOf(),
    firstAirDate = getDateCustomFormat(null,firstAirDate),
    genres = genres?.asGenres()?: emptyList(),
    seasons = seasons?.asSeasonsEntity()?: emptyList(),
    homepage = homepage?:"",
    inProduction = inProduction?: false,
    languages = languages?: emptyList(),
    lastAirDate = lastAirDate,
    numberOfEpisodes = numberOfEpisodes?:0,
    numberOfSeasons = numberOfSeasons?:0,
    originCountry = originCountry?: emptyList(),
    originalLanguage = originalLanguage?:"",
    originalName = originalName?:"",
    overview = overview?:"",
    popularity = popularity?:0.0,
    posterPath = posterPath?.asImageURL(),
    status = status?:"",
    tagline = tagline?:"",
    type = type?:"",
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    credits = credits?.asCredits()?: CreditsEntity(emptyList(), emptyList()),
    rating = voteAverage?.getRating()?:0.0,
)

fun TvShowDetailsEntity.asTvShowDetailsModel(isWishListed: Boolean) = TvShowDetailModel(
    id = id,
    name = name,
    adult = adult,
    backdropPath = backdropPath,
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    genres = genres.asGenreModels(),
    seasons = seasons.asSeasonModels(),
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
    credits = credits.asCreditsModel(),
    rating = rating,
    isWishListed = isWishListed,
)