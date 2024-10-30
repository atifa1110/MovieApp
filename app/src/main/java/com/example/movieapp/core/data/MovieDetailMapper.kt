package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getFormatReleaseDate
import com.example.movieapp.core.network.getRating
import com.example.movieapp.core.network.response.MovieDetailNetwork

fun MovieDetailNetwork.asMovieDetailsEntity() = MovieDetailsEntity(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    budget = budget,
    genreEntities = genres.asGenres(),
    homepage = homepage,
    imdbId = imdbId,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath?.asImageURL(),
    releaseDate = releaseDate?.getFormatReleaseDate(),
    revenue = revenue,
    runtime = runtime,
    status = status,
    tagline = tagline,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    rating =  voteAverage.getRating(),
    credits = credits.asCredits(),
    videos = videos.asVideoEntity(),
    images = images.asImagesEntity()
)

fun MovieDetailsEntity.asMovieDetailsModel(isWishListed: Boolean) = MovieDetailModel(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    budget = budget,
    genres = genreEntities.asGenreModels(),
    homepage = homepage,
    imdbId = imdbId,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    revenue = revenue,
    runtime = runtime,
    status = status,
    tagline = tagline,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    rating = rating,
    credits = credits.asCreditsModel(),
    images = images.asImagesModel(),
    videos = videos.asVideoModel(),
    isWishListed = isWishListed
)