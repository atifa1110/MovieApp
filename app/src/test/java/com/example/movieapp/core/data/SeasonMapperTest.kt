package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.tv.SeasonEntity
import com.example.movieapp.core.domain.SeasonModel
import com.example.movieapp.core.network.response.tv.NetworkSeason
import org.junit.Assert.assertEquals
import org.junit.Test

class SeasonMapperTest {

    @Test
    fun `asSeasonsEntity should map NetworkSeason list to SeasonEntity list`() {
        val networkSeasons = listOf(
            NetworkSeason(id = 1, name = "Season 1", overview = "overview", airDate = "2000-10-11", episodeCount = 10,
                seasonNumber = 10,posterPath = "", voteAverage = 0.0),
            NetworkSeason(id = 2, name = "Season 2", overview = "overview", airDate = "2000-10-11", episodeCount = 10,
                seasonNumber = 10,posterPath = "", voteAverage = 0.0),
        )

        val expectedEntities = listOf(
            SeasonEntity(id = 1, name = "Season 1", airDate = "October 11, 2000", episodeCount = 10, overview = "overview",
                posterPath = "https://image.tmdb.org/t/p/w500", seasonNumber = 10, rating="0"),
            SeasonEntity(id = 2, name = "Season 2", airDate = "October 11, 2000", episodeCount = 10, overview = "overview",
                posterPath = "https://image.tmdb.org/t/p/w500", seasonNumber = 10, rating="0"),
        )

        val result = networkSeasons.asSeasonsEntity()

        assertEquals(expectedEntities, result)
    }

    @Test
    fun `asSeasonModels should map SeasonEntity list to SeasonModel list`() {
        val seasonEntities = listOf(
            SeasonEntity(id = 1, name = "Season 1", airDate = "", episodeCount = 10, overview = "overview", posterPath = "", seasonNumber = 10, rating="0.0"),
            SeasonEntity(id = 2, name = "Season 2", airDate = "", episodeCount = 10, overview = "overview", posterPath = "", seasonNumber = 10, rating="0.0"),
        )

        val expectedModels = listOf(
            SeasonModel(id = 1, name = "Season 1",airDate = "", episodeCount = 10, overview = "overview", posterPath = "", seasonNumber = 10, rating="0.0"),
            SeasonModel(id = 2, name = "Season 2", airDate = "", episodeCount = 10, overview = "overview", posterPath = "", seasonNumber = 10, rating="0.0")
        )

        val result = seasonEntities.asSeasonModels()

        assertEquals(expectedModels, result)
    }
}
