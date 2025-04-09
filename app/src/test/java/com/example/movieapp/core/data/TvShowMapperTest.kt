package com.example.movieapp.core.data

import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.network.response.tv.TvShowNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class TvShowMappingTest {

    @Test
    fun `test TvShowNetwork to TvShowEntity without runtime`() {
        val tvShowNetwork = TvShowNetwork(
            id = 1,
            name = "Test Show",
            overview = "Test Overview",
            popularity = 9.5,
            firstAirDate = "2025-01-01",
            genreIds = emptyList(),
            originalName = "Test Original Name",
            originalLanguage = "en",
            originCountry = listOf("US"),
            voteAverage = 8.0,
            voteCount = 100,
            posterPath = "/testPoster.jpg",
            backdropPath = "/testBackdrop.jpg"
        )

        val tvShowEntity = tvShowNetwork.asTvShowEntity(MediaType.TvShow.Popular)

        assertEquals(tvShowNetwork.id, tvShowEntity.networkId)
        assertEquals(tvShowNetwork.name, tvShowEntity.name)
        assertEquals(tvShowNetwork.overview, tvShowEntity.overview)
        assertEquals(tvShowNetwork.firstAirDate, tvShowEntity.firstAirDate)
    }

    @Test
    fun `test TvShowNetwork to TvShowEntity with runtime`() {
        val tvShowNetwork = TvShowNetwork(
            id = 2,
            name = "Another Show",
            overview = "Another Overview",
            popularity = 7.5,
            firstAirDate ="2025-01-01",
            genreIds = emptyList(),
            originalName = "Original Another Show",
            originalLanguage = "fr",
            originCountry = listOf("FR"),
            voteAverage = 7.5,
            voteCount = 250,
            posterPath = "/anotherPoster.jpg",
            backdropPath = "/anotherBackdrop.jpg"
        )

        val tvShowEntity = tvShowNetwork.asTvShowEntity(MediaType.TvShow.Popular, 45)

        assertEquals(tvShowNetwork.id, tvShowEntity.networkId)
        assertEquals(tvShowNetwork.name, tvShowEntity.name)
        assertEquals(tvShowNetwork.overview, tvShowEntity.overview)
        assertEquals(tvShowNetwork.firstAirDate, tvShowEntity.firstAirDate)
    }
}
