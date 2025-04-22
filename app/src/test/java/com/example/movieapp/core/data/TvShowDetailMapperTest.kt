package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailMovie.CreditsEntity
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.network.getFormatReleaseDate
import com.example.movieapp.core.network.response.tv.TvShowDetailNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class TvShowDetailMapperTest {

    @Test
    fun `TvShowDetailNetwork maps correctly to TvShowDetailsEntity`() {
        // Given
        val networkModel = TvShowDetailNetwork(
            id = 123,
            name = "Sample Show",
            adult = false,
            backdropPath = "sample_backdrop.jpg",
            episodeRunTime = listOf(45),
            firstAirDate = "2023-01-01",
            genres = null, // Should default to empty list
            seasons = null, // Should default to empty list
            homepage = "https://example.com",
            inProduction = true,
            languages = listOf("en"),
            lastAirDate = "2023-12-01",
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Original Sample Show",
            overview = "A great show!",
            popularity = 99.9,
            posterPath = "sample_poster.jpg",
            status = "Running",
            tagline = "Best show ever!",
            type = "Scripted",
            voteAverage = 8.5,
            voteCount = 1000,
            credits = null // Should default to empty CreditsEntity
        )

        // When
        val entity = networkModel.asTvShowDetailsEntity()

        // Then
        assertEquals(networkModel.id, entity.id)
        assertEquals(networkModel.name, entity.name)
        assertEquals(networkModel.adult, entity.adult)
        assertEquals(networkModel.backdropPath, entity.backdropPath)
        assertEquals(networkModel.episodeRunTime, entity.episodeRunTime)
        assertEquals(networkModel.firstAirDate?.getFormatReleaseDate(), entity.firstAirDate)
        assertEquals(networkModel.homepage, entity.homepage)
    }

    @Test
    fun `TvShowDetailsEntity maps correctly to TvShowDetailModel`() {
        // Given
        val entity = TvShowDetailsEntity(
            id = 456,
            name = "Another Show",
            adult = true,
            backdropPath = "another_backdrop.jpg",
            episodeRunTime = listOf(30, 40),
            firstAirDate = "2022-06-15",
            genres = emptyList(),
            seasons = emptyList(),
            homepage = "https://anotherexample.com",
            inProduction = false,
            languages = listOf("fr"),
            lastAirDate = "2022-12-15",
            numberOfEpisodes = 20,
            numberOfSeasons = 2,
            originCountry = listOf("FR"),
            originalLanguage = "fr",
            originalName = "Original Another Show",
            overview = "A thrilling series!",
            popularity = 88.8,
            posterPath = "another_poster.jpg",
            status = "Ended",
            tagline = "Unforgettable journey!",
            type = "Reality",
            voteAverage = 7.2,
            voteCount = 500,
            credits = CreditsEntity(emptyList(), emptyList()),
            rating = 7.2
        )

        // When
        val model = entity.asTvShowDetailsModel(isWishListed = true)

        // Then
        assertEquals(entity.id, model.id)
        assertEquals(entity.name, model.name)
        assertEquals(entity.adult, model.adult)
        assertEquals(entity.backdropPath, model.backdropPath)
        assertEquals(entity.episodeRunTime, model.episodeRunTime)
        assertEquals(entity.firstAirDate, model.firstAirDate)
        assertEquals(entity.homepage, model.homepage)
    }
}
