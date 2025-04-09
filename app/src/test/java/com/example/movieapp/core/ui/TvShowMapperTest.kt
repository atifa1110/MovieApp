package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.TvShowDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class TvShowDetailsMapperTest {

    @Test
    fun `asTvShowDetailModel maps TvShowDetails to TvShowDetailModel correctly`() {
        // Given
        val tvShowDetails = TvShowDetails(
            id = 1,
            name = "Test Show",
            adult = false,
            backdropPath = "/backdrop.jpg",
            episodeRunTime = listOf(45),
            firstAirDate = "2023-01-01",
            genres = listOf(),
            seasons = listOf(),
            homepage = "http://homepage.com",
            inProduction = false,
            languages = listOf("en"),
            lastAirDate = "2023-12-31",
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Original Test Show",
            overview = "Test overview",
            popularity = 88.0,
            posterPath = "/poster.jpg",
            status = "Ended",
            tagline = "Test Tagline",
            type = "Scripted",
            voteAverage = 8.5,
            voteCount = 200,
            credits = Credits(emptyList(), emptyList()), // gets overridden to empty CreditsModel in mapping
            rating = 0.0,
            isWishListed = true
        )

        // When
        val model = tvShowDetails.asTvShowDetailModel()

        // Then
        assertEquals(tvShowDetails.id, model.id)
        assertEquals(tvShowDetails.name, model.name)
        assertEquals(tvShowDetails.adult, model.adult)
        assertEquals(true, model.credits.cast.isEmpty())
        assertEquals(true, model.credits.crew.isEmpty())
    }

    @Test
    fun `asTvShow maps TvShowModel to TvShow correctly`() {
        // Given
        val model = TvShowModel(
            id = 101,
            name = "Test TV Show",
            popularity = 0.0,
            overview = "This is a test show overview.",
            firstAirDate = "2023-03-15",
            genres = emptyList(), // assuming asGenres() maps correctly to empty list
            originalName = "",
            originalLanguage = "",
            originCountry = listOf(),
            voteAverage = 8.2,
            voteCount = 0,
            posterPath = "/poster_test.jpg",
            backdropPath = "/backdrop_test.jpg",
            rating = 4.7
        )

        // When
        val result = model.asTvShow()

        // Then
        assertEquals(model.id, result.id)
        assertEquals(model.name, result.name)
        assertEquals(model.overview, result.overview)
        assertEquals(model.firstAirDate, result.firstAirDate)
        assertEquals(model.genres.size, result.genres.size) // asGenres() maps emptyList

    }
}
