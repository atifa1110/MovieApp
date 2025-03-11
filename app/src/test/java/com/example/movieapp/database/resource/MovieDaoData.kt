package com.example.movieapp.database.resource

import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.util.MediaType

val movies = listOf(
    MovieEntity(
        id  = 0,
        mediaType = MediaType.Movie.Popular,
        networkId = 845781,
        title = "Red One" ,
        overview = "After Santa Claus (codename: Red One) is kidnapped, the North Pole's Head of Security must team up with the world's most infamous tracker in a globe-trotting, action-packed mission to save Christmas.",
        popularity= 4760.061,
        releaseDate ="2024-10-31",
        adult= false,
        genreIds= emptyList(),
        originalTitle = "Red One",
        originalLanguage = "en",
        voteAverage= 7.031,
        voteCount = 1516,
        posterPath = "/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
        backdropPath = "/cjEcqdRdPQJhYre3HUAc5538Gk8.jpg",
        video = false,
        rating = 70.0,
        runtime = 120
    ),
    MovieEntity(
        id  = 1,
        mediaType = MediaType.Movie.Popular,
        networkId = 845781,
        title = "Red One" ,
        overview = "After Santa Claus (codename: Red One) is kidnapped, the North Pole's Head of Security must team up with the world's most infamous tracker in a globe-trotting, action-packed mission to save Christmas.",
        popularity= 4760.061,
        releaseDate ="2024-10-31",
        adult= false,
        genreIds= emptyList(),
        originalTitle = "Red One",
        originalLanguage = "en",
        voteAverage= 7.031,
        voteCount = 1516,
        posterPath = "/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
        backdropPath = "/cjEcqdRdPQJhYre3HUAc5538Gk8.jpg",
        video = false,
        rating = 70.0,
        runtime = 120
    )
)

val trendingMovies = listOf(
    MovieEntity(
        id  = 0,
        mediaType = MediaType.Movie.Trending,
        networkId = 845781,
        title = "Red One" ,
        overview = "After Santa Claus (codename: Red One) is kidnapped, the North Pole's Head of Security must team up with the world's most infamous tracker in a globe-trotting, action-packed mission to save Christmas.",
        popularity= 4760.061,
        releaseDate ="2024-10-31",
        adult= false,
        genreIds= emptyList(),
        originalTitle = "Red One",
        originalLanguage = "en",
        voteAverage= 7.031,
        voteCount = 1516,
        posterPath = "/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
        backdropPath = "/cjEcqdRdPQJhYre3HUAc5538Gk8.jpg",
        video = false,
        rating = 70.0,
        runtime = 120
    ),
    MovieEntity(
        id  = 1,
        mediaType = MediaType.Movie.Trending,
        networkId = 845781,
        title = "Red One" ,
        overview = "After Santa Claus (codename: Red One) is kidnapped, the North Pole's Head of Security must team up with the world's most infamous tracker in a globe-trotting, action-packed mission to save Christmas.",
        popularity= 4760.061,
        releaseDate ="2024-10-31",
        adult= false,
        genreIds= emptyList(),
        originalTitle = "Red One",
        originalLanguage = "en",
        voteAverage= 7.031,
        voteCount = 1516,
        posterPath = "/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
        backdropPath = "/cjEcqdRdPQJhYre3HUAc5538Gk8.jpg",
        video = false,
        rating = 70.0,
        runtime = 120
    )
)

val movieDetail = MovieDetailsEntity(
    id = 845781,
    adult = false,
    backdropPath = "/cjEcqdRdPQJhYre3HUAc5538Gk8.jpg",
    budget = 0,
    genreEntities = emptyList(),
    homepage = "https://www.amazon.com/salp/redonemovie",
    imdbId = "tt14948432",
    originalLanguage = "en",
    originalTitle = "Red One",
    overview = "After Santa Claus (codename: Red One) is kidnapped, the North Pole's Head of Security must team up with the world's most infamous tracker in a globe-trotting, action-packed mission to save Christmas.",
    popularity = 4760.061,
    posterPath = "/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
    releaseDate = "2024-10-31",
    revenue = 182861176,
    runtime = 124,
    status = "Released",
    tagline = "The mission to save Christmas is on.",
    title = "Red One",
    video = false,
    voteAverage = 7.032,
    voteCount = 1550,
    rating = 89.0,
    credits =  CreditsEntity(emptyList(), emptyList()),
    images = ImagesEntity(emptyList(), emptyList()),
    videos = VideosEntity(emptyList())
)

val tvShows = listOf(
    TvShowEntity(
        id = 93405,
        mediaType = MediaType.TvShow.Popular,
        networkId =  93405,
        name = "Squid Game",
        overview = "Hundreds of cash-strapped players accept a strange invitation to compete in children's games. Inside, a tempting prize awaits — with deadly high stakes.",
        popularity = 3138.471,
        firstAirDate = "2021-09-17",
        genres = emptyList(),
        originalName = "오징어 게임",
        originalLanguage = "ko",
        originCountry =  listOf("KR"),
        voteAverage = 7.835,
        voteCount = 14299,
        posterPath = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
        backdropPath = "/2meX1nMdScFOoV4370rqHWKmXhY.jpg",
        runtime = 124,
        rating = 0.0
    )
)

val trendingTvShows = listOf(
    TvShowEntity(
        id =  91363,
        mediaType = MediaType.TvShow.Trending,
        networkId =  91363,
        name = "What If...?",
        overview =  "Taking inspiration from the comic books of the same name, each episode of this animated anthology series questions, revisits and twists classic Marvel Cinematic moments.",
        popularity = 1670.462,
        firstAirDate = "2021-08-11",
        genres = emptyList(),
        originalName = "What If...?",
        originalLanguage = "en",
        originCountry =  listOf("US"),
        voteAverage = 7.835,
        voteCount = 14299,
        posterPath = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
        backdropPath = "/2meX1nMdScFOoV4370rqHWKmXhY.jpg",
        runtime = 124,
        rating = 0.0
    )
)

val tvShowDetails = TvShowDetailsEntity(
    id = 93405,
    name = "Squid Game",
    adult = false,
    backdropPath =  "/2meX1nMdScFOoV4370rqHWKmXhY.jpg",
    episodeRunTime = emptyList(),
    firstAirDate= "2021-09-17",
    genres = emptyList(),
    seasons = emptyList(),
    homepage = "https://www.netflix.com/title/81040344",
    inProduction = false,
    languages = emptyList(),
    lastAirDate = "2024-12-26",
    numberOfEpisodes = 0,
    numberOfSeasons = 0,
    originCountry = emptyList(),
    originalLanguage = "ko",
    originalName = "오징어 게임",
    overview =  "Hundreds of cash-strapped players accept a strange invitation to compete in children's games. Inside, a tempting prize awaits — with deadly high stakes.",
    popularity = 3138.471,
    posterPath = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
    status = "Returning Series",
    tagline = "Let the new games begin.",
    type =  "Scripted",
    voteAverage =  7.835,
    voteCount = 14333,
    credits = CreditsEntity(emptyList(), emptyList()),
    rating = 80.0
)