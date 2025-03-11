package com.example.movieapp.core.ui

import com.example.movieapp.core.data.asCreditsModel
import com.example.movieapp.core.data.asGenreModels
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.model.TvShow
import com.example.movieapp.core.model.TvShowDetails
import com.example.movieapp.core.network.asImageURL


fun TvShowDetails.asTvShowDetailModel() = TvShowDetailModel(
    id = id,
    name = name,
    adult = adult,
    backdropPath = backdropPath,
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    genres = genres.asGenreModel(),
    seasons = seasons.asSeasonModel(),
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
    credits = CreditsModel(emptyList(), emptyList()),
    rating = rating,
    isWishListed = isWishListed
)

fun TvShowModel.asTvShow() = TvShow(
    id = id,
    name = name,
    overview = overview,
    firstAirDate = firstAirDate,
    genres = genres.asGenres(),
    voteAverage = voteAverage,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
)
