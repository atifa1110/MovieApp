package com.example.movieapp.core.ui

import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.Images
import com.example.movieapp.core.model.Videos

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.MovieDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDetailsMapperTest {

    @Test
    fun `test MovieDetails asMovieDetailModel maps correctly`() {
        // Given: A MovieDetails object
        val movieDetails = MovieDetails(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            backdropPath = "/backdrop.jpg",
            budget = 160_000_000,
            genres = emptyList(), // Mock genre
            posterPath = "/poster.jpg",
            releaseDate = "2010-07-16",
            runtime = 148,
            video = false,
            voteAverage = 8.8,
            voteCount = 20000,
            rating = 5.0,
            credits = Credits(emptyList(), emptyList()),
            images = Images(emptyList(), emptyList()),
            videos = Videos(emptyList()),
            isWishListed = true
        )

        // When: Mapping occurs
        val result = movieDetails.asMovieDetailModel()

        // Then: Assert fields are correctly mapped
        assertEquals(movieDetails.id, result.id)
        assertEquals(movieDetails.title, result.title)
        assertEquals(movieDetails.overview, result.overview)
        assertEquals(movieDetails.backdropPath, result.backdropPath)
        assertEquals(movieDetails.budget, result.budget)

        // Check empty default values
        assertEquals("", result.homepage)
        assertEquals("", result.imdbId)
        assertEquals("", result.originalLanguage)
        assertEquals("", result.originalTitle)
    }

    @Test
    fun `test MovieDetailModel asMovieDetails maps correctly`() {
        // Given: A MovieDetailModel object
        val movieDetailModel = MovieDetailModel(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            backdropPath = "/backdrop.jpg",
            budget = 160_000_000,
            genres = listOf(GenreModel.ACTION), // Mock genre
            posterPath = "/poster.jpg",
            releaseDate = "2010-07-16",
            runtime = 148,
            video = false,
            voteAverage = 8.8,
            voteCount = 20000,
            rating = 5.0,
            credits = CreditsModel(emptyList(), emptyList()),
            images = ImagesModel(emptyList(), emptyList()),
            videos = VideosModel(emptyList()),
            isWishListed = true,
            adult = false,
            homepage = "https://inception.com",
            imdbId = "tt1375666",
            originalLanguage = "en",
            originalTitle = "Inception",
            popularity = 91.0,
            revenue = 830_000_000,
            status = "Released",
            tagline = "Your mind is the scene of the crime."
        )

        // When: Mapping occurs
        val result = movieDetailModel.asMovieDetails()

        // Then: Assert fields are correctly mapped
        assertEquals(movieDetailModel.id, result.id)
        assertEquals(movieDetailModel.title, result.title)
        assertEquals(movieDetailModel.overview, result.overview)
        assertEquals(movieDetailModel.backdropPath, result.backdropPath)
        assertEquals(movieDetailModel.budget, result.budget)
        assertEquals(movieDetailModel.posterPath, result.posterPath)
        assertEquals(movieDetailModel.releaseDate, result.releaseDate)
        assertEquals(movieDetailModel.runtime, result.runtime)
        assertEquals(movieDetailModel.video, result.video)
        assertEquals(movieDetailModel.voteAverage, result.voteAverage, 0.01)
        assertEquals(movieDetailModel.voteCount, result.voteCount)
        assertEquals(movieDetailModel.rating, result.rating, 0.01)
        assertEquals(movieDetailModel.isWishListed, result.isWishListed)
    }

    @Test
    fun `test MovieDetailModel asMovieDetails handles null runtime correctly`() {
        // Given: A MovieDetailModel object with null runtime
        val movieDetailModel = MovieDetailModel(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            backdropPath = "/backdrop.jpg",
            budget = 160_000_000,
            genres = listOf(),
            posterPath = "/poster.jpg",
            releaseDate = "2010-07-16",
            runtime = null, // Null runtime
            video = false,
            voteAverage = 8.8,
            voteCount = 20000,
            rating = 5.0,
            credits = CreditsModel(emptyList(), emptyList()),
            images = ImagesModel(emptyList(), emptyList()),
            videos = VideosModel(emptyList()),
            isWishListed = true,
            adult = false,
            homepage = "",
            imdbId = "",
            originalLanguage = "",
            originalTitle = "",
            popularity = 0.0,
            revenue = 0,
            status = "",
            tagline = ""
        )

        // When: Mapping occurs
        val result = movieDetailModel.asMovieDetails()

        // Then: Assert runtime defaults to NoMovieRuntimeValue
        assertEquals(NoMovieRuntimeValue, result.runtime)
    }
}
