package com.example.movieapp.core.data


import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.response.movies.MovieNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {

    @Test
    fun `asMovieEntity should correctly map MovieNetwork to MovieEntity`() {
        // Given: A sample MovieNetwork object
        val networkMovie = MovieNetwork(
            id = 1,
            originalTitle = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            voteAverage = 8.0
        )

        // When: Mapping is applied
        val entity = networkMovie.asMovieEntity(MediaType.Movie.Popular)

        // Then: Verify key fields
        assertEquals(networkMovie.id, entity.networkId)
        assertEquals(networkMovie.originalTitle, entity.title)
    }

    @Test
    fun `asMovieEntity should correctly map MovieNetwork runtime to MovieEntity`() {
        // Given: A sample MovieNetwork object
        val networkMovie = MovieNetwork(
            id = 1,
            originalTitle = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            voteAverage = 8.0
        )

        // When: Mapping is applied
        val entity = networkMovie.asMovieEntity(MediaType.Movie.Popular,120)

        // Then: Verify key fields
        assertEquals(networkMovie.id, entity.networkId)
        assertEquals(networkMovie.originalTitle, entity.title)
        assertEquals(120, entity.runtime)
    }

    @Test
    fun `asMovieModel should correctly map MovieEntity to MovieModel`() {
        // Given: A sample MovieEntity object
        val entityMovie = MovieEntity(
            id = 1,
            mediaType = MediaType.Movie.Popular,
            networkId = 1,
            title = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            releaseDate = "",
            adult = false,
            genreIds = emptyList(),
            originalTitle = "",
            originalLanguage = "",
            voteAverage = 8.0,
            voteCount = 0,
            posterPath = "/poster/jpg",
            backdropPath = "",
            video = false,
            rating = 9.0,
            runtime = 120,
        )

        // When: Mapping is applied
        val model = entityMovie.asMovieModel()

        // Then: Verify key fields
        assertEquals(entityMovie.networkId, model.id)
        assertEquals(entityMovie.title, model.title)
    }

    @Test
    fun `asMovieModel should correctly map MovieWithGenres to MovieModel`() {
        // Given: A sample MovieEntity object
        val entityMovie = MovieEntity(
            id = 1,
            mediaType = MediaType.Movie.Popular,
            networkId = 1,
            title = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            releaseDate = "",
            adult = false,
            genreIds = emptyList(),
            originalTitle = "",
            originalLanguage = "",
            voteAverage = 8.0,
            voteCount = 0,
            posterPath = "/poster/jpg",
            backdropPath = "",
            video = false,
            rating = 9.0,
            runtime = 120,
        )
        val movieGenres = MovieWithGenres(
            movie = entityMovie,
            genreNames = listOf(
                "Action","Adventure"
            )
        )

        // When: Mapping is applied
        val model = movieGenres.asMovieModel()

        // Then: Verify key fields
        assertEquals(entityMovie.networkId, model.id)
        assertEquals(entityMovie.title, model.title)
    }

    @Test
    fun `asMovieModel should correctly map MovieNetwork to MovieModel`() {
        // Given: A sample MovieEntity object
        val networkMovie = MovieNetwork(
            id = 1,
            originalTitle = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            title = "Sample Movie",
            voteAverage = 8.0,
        )

        // When: Mapping is applied
        val model = networkMovie.asMovieModel()

        // Then: Verify key fields
        assertEquals(networkMovie.id, model.id)
        assertEquals(networkMovie.title, model.title)
    }

    @Test
    fun `asMovieModel should correctly map MovieNetwork runtime to MovieModel`() {
        // Given: A sample MovieEntity object
        val networkMovie = MovieNetwork(
            id = 1,
            originalTitle = "Sample Movie",
            overview = "This is a test movie",
            popularity = 9.5,
            voteAverage = 8.0,
            title = "Sample Movie"
        )

        // When: Mapping is applied
        val model = networkMovie.asMovieModel(120)

        // Then: Verify key fields
        assertEquals(networkMovie.id, model.id)
        assertEquals(networkMovie.title, model.title)
        assertEquals(120, model.runtime)
    }
}
