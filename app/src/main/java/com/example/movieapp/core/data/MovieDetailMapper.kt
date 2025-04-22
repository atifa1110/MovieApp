package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailMovie.CreditsEntity
import com.example.movieapp.core.database.model.detailMovie.ImagesEntity
import com.example.movieapp.core.database.model.detailMovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.detailMovie.VideosEntity
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.getFormatReleaseDate
import com.example.movieapp.core.network.getRating
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork

fun MovieDetailNetwork.asMovieDetailsEntity() = MovieDetailsEntity(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    budget = budget?:0,
    genreEntities = genres?.asGenres()?: listOf(),
    homepage = homepage,
    imdbId = imdbId,
    originalLanguage = originalLanguage?:"",
    originalTitle = originalTitle?:"",
    overview = overview?:"",
    popularity = popularity?:0.0,
    posterPath = posterPath?.asImageURL(),
    releaseDate = releaseDate?.getFormatReleaseDate(),
    revenue = revenue?:0,
    runtime = runtime,
    status = status?:"",
    tagline = tagline,
    title = title?:"",
    video = video?:false,
    voteAverage = voteAverage?:0.0,
    voteCount = voteCount?:0,
    rating =  voteAverage?.getRating()?:0.0,
    credits = credits?.asCredits()?: CreditsEntity(listOf(), listOf()),
    videos = videos?.asVideoEntity()?: VideosEntity(listOf()),
    images = images?.asImagesEntity()?: ImagesEntity(listOf(), listOf())
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