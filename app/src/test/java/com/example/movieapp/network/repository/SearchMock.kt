package com.example.movieapp.network.repository

import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.network.response.tv.TvShowNetwork
import com.example.movieapp.core.network.response.tv.TvShowResponse


fun createMockMovieNetworkModel(id: Int, mediaType: String = "movie"): MovieNetwork {
        return MovieNetwork(
            adult = false,
            genreIds = emptyList(),
            id = id,
            originalLanguage = "en",
            originalTitle = "Title $id",
            overview = "Overview $id",
            popularity = 7.5,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            profilePath = "/profile.jpg",
            releaseDate = "2023-10-26",
            title = "Title $id",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            mediaType = mediaType,
            name = null,
            firstAirDate = null,
            originalName = null
        )
    }

    fun createMockMovieModel(id: Int): MovieModel {
        return MovieModel(
            id = id,
            title = "Title $id",
            overview = "Overview $id",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            profilePath = "https://image.tmdb.org/t/p/w500/profile.jpg",
            releaseDate = "October 26, 2023",
            voteAverage = 8.0,
            voteCount = 100,
            runtime = 120,
            mediaType = "Movie",
            genres = emptyList(),
            rating = 4.0,
            popularity = 7.5,
            originalTitle = "Title $id",
            originalLanguage = "en"
        )
    }

    fun createMockDetailMovieModel(id: Int, runtime: Int): MovieDetailNetwork {
        return MovieDetailNetwork(
            id = id,
            adult = false,
            title = "Title $id",
            overview = "Overview $id",
            posterPath = "/poster.jpg",
            releaseDate = "2023-10-26",
            voteAverage = 8.0,
            runtime = runtime,
        )
    }

    fun createMockMovieResponse(movies: List<MovieNetwork>): MovieResponse {
        return MovieResponse(
            page = 1,
            results = movies,
            totalPages = 1,
            totalResults = movies.size
        )
    }

    fun createMockTvShowModel(id: Int) : TvShowModel{
        return TvShowModel(
            id = id,
            name = "Tv Show",
            overview = "",
            popularity = 0.0,
            firstAirDate = "",
            genres = emptyList(),
            originalName = "",
            originalLanguage = "",
            originCountry = emptyList(),
            voteAverage = 0.0,
            voteCount = 0,
            posterPath = "https://image.tmdb.org/t/p/w500",
            backdropPath = "https://image.tmdb.org/t/p/w500",
            rating = 1.0
        )
    }

    fun createMockTvShowNetworkModel(id: Int): TvShowNetwork {
        return TvShowNetwork(
            id = id,
            name = "Tv Show",
            overview = "",
            popularity = 0.0,
            firstAirDate = "",
            genreIds = emptyList(),
            originalName = "",
            originalLanguage = "",
            originCountry = emptyList(),
            voteAverage = 0.0,
            voteCount = 0,
            posterPath = "",
            backdropPath = ""
        )
    }

    fun createMockTvResponse(tvs: List<TvShowNetwork>): TvShowResponse {
        return TvShowResponse(
            page = 1,
            results = tvs,
            totalPages = 1,
            totalResults = tvs.size
        )
    }

    fun createMockTvShowEntity(id: Int, mediaType: MediaType.TvShow): TvShowEntity { // Changed parameter type
        return TvShowEntity(
            id = id,
            mediaType = mediaType,
            networkId = id,
            name = "Tv Show",
            overview = "",
            popularity = 0.0,
            firstAirDate = "",
            genres = emptyList(),
            originalName = "",
            originalLanguage = "",
            originCountry = emptyList(),
            voteAverage = 0.0,
            voteCount = 0,
            posterPath = "https://image.tmdb.org/t/p/w500",
            backdropPath = "https://image.tmdb.org/t/p/w500",
            runtime = 0,
            rating = 1.0
        )
    }