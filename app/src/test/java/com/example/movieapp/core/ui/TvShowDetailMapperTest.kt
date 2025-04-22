package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.*
import com.example.movieapp.core.model.TvShowDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class TvShowDetailMapperTest {

    @Test
    fun `asTvShowDetails should correctly map TvShowDetailModel to TvShowDetails`() {
        // Given
        val model = TvShowDetailModel(
            id = 1,
            name = "Test Show",
            adult = false,
            backdropPath = "/test_backdrop.jpg",
            episodeRunTime = listOf(45),
            firstAirDate = "2023-01-01",
            genres = listOf(GenreModel.ACTION),
            seasons = listOf(
                SeasonModel(
                    id = 1,
                    name = "Season 1",
                    airDate = "2023-01-01",
                    episodeCount = 10,
                    seasonNumber = 1,
                    posterPath = "/poster.jpg",
                    overview = "",
                    rating = ""
                )
            ),
            homepage = "http://testhomepage.com",
            inProduction = true,
            languages = listOf("en"),
            lastAirDate = "2023-12-31",
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Original Test Show",
            overview = "A test overview",
            popularity = 99.0,
            posterPath = "/test_poster.jpg",
            status = "Ended",
            tagline = "Test tagline",
            type = "Scripted",
            voteAverage = 8.5,
            voteCount = 100,
            credits = CreditsModel(
                cast = emptyList(),
                crew = emptyList()
            ),
            rating = 0.0,
            isWishListed = false
        )

        // When
        val result: TvShowDetails = model.asTvShowDetails()

        // Then
        assertEquals(model.id, result.id)
        assertEquals(model.name, result.name)
        assertEquals(model.adult, result.adult)
        assertEquals(model.backdropPath, result.backdropPath)
        assertEquals(model.episodeRunTime, result.episodeRunTime)
    }
}
