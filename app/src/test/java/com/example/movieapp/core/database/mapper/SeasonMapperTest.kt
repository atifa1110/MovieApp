package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.database.model.tv.SeasonEntity
import com.example.movieapp.core.domain.SeasonModel
import com.example.movieapp.core.network.response.tv.NetworkSeason
import org.junit.Assert.*
import org.junit.Test

class SeasonMapperTest {

    @Test
    fun `NetworkSeason asSeasonEntity returns correct SeasonEntity`() {
        val networkSeason = NetworkSeason(
            airDate = "2023-05-10",
            episodeCount = 10,
            id = 101,
            name = "Season 1",
            overview = "The beginning of a great show",
            posterPath = "/poster.jpg",
            seasonNumber = 1,
            voteAverage = 8.5
        )

        val entity = networkSeason.asSeasonEntity()

        assertEquals("May 10, 2023", entity.airDate)
        assertEquals(10, entity.episodeCount)
        assertEquals(101, entity.id)
        assertEquals("Season 1", entity.name)
        assertEquals("The beginning of a great show", entity.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", entity.posterPath)
        assertEquals(1, entity.seasonNumber)
    }

    @Test
    fun `NetworkSeason asSeasonEntity handles null values`() {
        val networkSeason = NetworkSeason(
            airDate = null,
            episodeCount = 0,
            id = 102,
            name = "Season 2",
            overview = "",
            posterPath = null,
            seasonNumber = 2,
            voteAverage = 0.0
        )

        val entity = networkSeason.asSeasonEntity()

        assertEquals("Coming Soon", entity.airDate)
        assertEquals(0, entity.episodeCount)
        assertEquals(102, entity.id)
        assertEquals("Season 2", entity.name)
        assertEquals("", entity.overview)
        assertEquals("", entity.posterPath)
        assertEquals(2, entity.seasonNumber)
    }

    @Test
    fun `SeasonEntity asSeasonModel returns correct SeasonModel`() {
        val entity = SeasonEntity(
            airDate = "2023-05-10",
            episodeCount = 10,
            id = 101,
            name = "Season 1",
            overview = "The beginning of a great show",
            posterPath = "/poster.jpg",
            seasonNumber = 1,
            rating = "85%"
        )

        val model = entity.asSeasonModel()

        assertEquals("2023-05-10", model.airDate)
        assertEquals(10, model.episodeCount)
        assertEquals(101, model.id)
        assertEquals("Season 1", model.name)
        assertEquals("The beginning of a great show", model.overview)
        assertEquals("/poster.jpg", model.posterPath)
        assertEquals(1, model.seasonNumber)
        assertEquals("85%", model.rating)
    }
}
