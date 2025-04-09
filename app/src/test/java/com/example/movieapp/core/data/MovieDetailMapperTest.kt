package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDetailMapperTest {

    @Test
    fun `asMovieDetailsEntity should correctly map MovieDetailNetwork to MovieDetailsEntity`() {
        // Given: A sample MovieDetailNetwork object
        val networkMovie = MovieDetailNetwork(
            id = 1,
            adult = false,
            backdropPath = "/test.jpg",
            budget = 1000000,
            homepage = "https://example.com",
            originalTitle = "Sample Movie",
            voteAverage = 8.5
        )

        // When: Mapping is applied
        val entity = networkMovie.asMovieDetailsEntity()

        // Then: Verify key fields
        assertEquals(networkMovie.id, entity.id)
        assertEquals(networkMovie.budget, entity.budget)
    }

    @Test
    fun `asMovieDetailsModel should correctly map MovieDetailsEntity to MovieDetailModel`() {
        // Given: A sample MovieDetailsEntity object
        val entityMovie = MovieDetailsEntity(
            id = 1,
            adult = false,
            backdropPath = "/test.jpg",
            budget = 1000000,
            genreEntities = emptyList(),
            homepage = "https://example.com",
            imdbId = null,
            originalLanguage = "",
            originalTitle = "Sample Movie",
            overview ="overview",
            popularity = 0.0,
            posterPath = "/poster.jpg",
            releaseDate = "",
            revenue = 0,
            runtime = 0,
            status = "",
            tagline ="",
            title ="Title",
            video =false,
            voteAverage = 8.5,
            voteCount = 0,
            rating = 4.0,
            credits = CreditsEntity(emptyList(), emptyList()),
            images = ImagesEntity(emptyList(), emptyList()),
            videos = VideosEntity(emptyList())
        )

        // When: Mapping is applied with isWishListed = true
        val model = entityMovie.asMovieDetailsModel(isWishListed = true)

        // Then: Verify key fields
        assertEquals(entityMovie.id, model.id)
        assertEquals(entityMovie.budget, model.budget)
    }
}