import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.network.response.tv.TvShowNetwork
import com.example.movieapp.core.network.response.tv.TvShowResponse


fun createMockMovieResponse(movies: List<MovieNetwork>): MovieResponse {
    return MovieResponse(
        page = 1,
        results = movies,
        totalPages = 1,
        totalResults = movies.size
    )
}

fun createMockMovieNetworkModelType(id: Int): MovieNetwork { // Corrected return type
    return MovieNetwork(
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = emptyList(),
        id = id,
        originalLanguage = "en",
        name = "Title $id",
        originalTitle = "Title $id",
        overview = "Overview $id",
        popularity = 7.5,
        posterPath = "/poster.jpg",
        profilePath = "/profile.jpg",
        mediaType = "movie",
        releaseDate = "2023-10-26",
        firstAirDate = "2023-10-26",
        title = "Title $id",
        video = false,
        voteAverage = 8.0,
        voteCount = 100,
    )
}


fun createMockMovieNetworkModel(id: Int): MovieNetwork { // Corrected return type
    return MovieNetwork(
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = emptyList(),
        id = id,
        originalLanguage = "en",
        name = "Title $id",
        originalTitle = "Title $id",
        overview = "Overview $id",
        popularity = 7.5,
        posterPath = "/poster.jpg",
        profilePath = "/profile.jpg",
        mediaType = "movie_popular",
        releaseDate = "2023-10-26",
        firstAirDate = "2023-10-26",
        title = "Title $id",
        video = false,
        voteAverage = 8.0,
        voteCount = 100,
    )
}

fun createMockMovieModel(id: Int): MovieModel {
    return MovieModel(
        id = id,
        title = "Title $id",
        overview = "Overview $id",
        profilePath = "https://image.tmdb.org/t/p/w500/profile.jpg",
        posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
        backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
        releaseDate = "October 26, 2023" ,
        voteAverage = 8.0,
        runtime = 0,
        mediaType = "Movie",
        originalTitle = "Title $id",
        originalLanguage = "en",
        voteCount = 100,
        popularity = 7.5,
        rating = 4.0
    )
}

fun createMockMovieEntity(id: Int, mediaType: MediaTypeModel.Movie): MovieEntity { // Changed parameter type
    return MovieEntity(
        id = id,
        title = "Title $id",
        networkId = id,
        overview = "Overview $id",
        adult = false,
        genreIds = emptyList(),
        originalLanguage = "en",
        originalTitle = "Title $id",
        popularity = 7.5,
        posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
        releaseDate = "October 26, 2023",
        voteAverage = 8.0,
        voteCount = 100,
        backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
        runtime = 0,
        video = false,           // Consistent with Network Model
        rating = 4.0,          // Consistent with Network and Movie Model (voteAverage)
        mediaType = mediaType.asMediaType()
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

//Tv Show

fun createMockTvShowModel(id: Int) : TvShowModel {
    return TvShowModel(
        id = id,
        name = "Tv Show $id",
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
        name = "Tv Show $id",
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

fun createMockTvShowEntity(id: Int, mediaType: com.example.movieapp.core.database.util.MediaType.TvShow): TvShowEntity { // Changed parameter type
    return TvShowEntity(
        id = id,
        mediaType = mediaType,
        networkId = id,
        name = "Tv Show $id",
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

