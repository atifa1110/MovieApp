package com.example.movieapp.network.repository

import com.example.movieapp.core.database.model.detailMovie.CreditsEntity
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.database.source.TvShowDetailsDatabaseSource
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.repository.TvShowDetailRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.tv.TvShowDetailNetwork
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class TvShowDetailRepositoryImplTest {

    // Mock dependencies
    private val databaseDataSource: TvShowDetailsDatabaseSource = mock()
    private val networkDataSource: TvShowNetworkDataSource = mock()
    private val wishlistDatabaseSource: WishlistDatabaseSource = mock()

    // Repository under test
    private lateinit var repository: TvShowDetailRepositoryImpl

    @Before
    fun setUp() {
        repository = TvShowDetailRepositoryImpl(
            databaseDataSource,
            networkDataSource,
            wishlistDatabaseSource
        )
    }

    @Test
    fun `getDetailTvShow should emit loading, fetch from network, save to DB, and return success`() = runTest {
        val tvId = 1

        val mockTvEntity = TvShowDetailsEntity(
            id = tvId, adult = false, backdropPath = "", name = "TvShow", episodeRunTime = emptyList(), firstAirDate = "",
            genres = emptyList(), seasons = emptyList(), homepage = "", inProduction = false, languages = emptyList(), lastAirDate = "",
            numberOfEpisodes = 0, numberOfSeasons = 0, originCountry = emptyList(), originalLanguage = "", originalName = "",
            overview = "This is a sample overview", popularity = 0.0, posterPath = "", status = "", tagline = "", type = "", voteAverage = 0.0,
            voteCount = 0,  rating = 0.0, credits = CreditsEntity(listOf(), listOf())
        )
        val mockTvDetail = TvShowDetailModel(
            id = tvId, adult = false, backdropPath = "", name = "TvShow", episodeRunTime = emptyList(), firstAirDate = "",
            genres = emptyList(), seasons = emptyList(), homepage = "", inProduction = false, languages = emptyList(), lastAirDate = "",
            numberOfEpisodes = 0, numberOfSeasons = 0, originCountry = emptyList(), originalLanguage = "", originalName = "",
            overview = "This is a sample overview", popularity = 0.0, posterPath = "https://image.tmdb.org/t/p/w500", status = "", tagline = "", type = "", voteAverage = 0.0,
            voteCount = 0, credits = CreditsModel(listOf(), listOf()), rating = 0.0, isWishListed = false
        )

        val mockApiResponse = TvShowDetailNetwork(
            id = tvId,
            name = "TvShow",
            adult = false,
            overview = "This is a sample overview",
        )

        // Mock database and network calls
        whenever(databaseDataSource.getById(tvId)).thenReturn(flowOf(mockTvEntity))
        whenever(networkDataSource.getDetailTv(tvId)).thenReturn(CinemaxResponse.Success(mockApiResponse))
        whenever(wishlistDatabaseSource.isTvShowWishListed(tvId)).thenReturn(false)

        val results = repository.getDetailTvShow(tvId).toList()
        // Assert
        assertEquals(3, results.size) // Loading, Loading, Success

        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(mockTvDetail, successResult.value) // Check if the final data is correct

        // Verify that the fetched movie details were saved to the database
        verify(databaseDataSource).deleteAndInsert(any())
    }

    @Test
    fun `getDetailMovie should emit failure when network fetch fails`() = runTest {
        val movieId = 1
        val errorMessage = "Network Error"

        // Mock DB returning null and network throwing an error
        whenever(databaseDataSource.getById(movieId)).thenReturn(flowOf(null))
        whenever(networkDataSource.getDetailTv(movieId)).thenReturn(CinemaxResponse.Failure("Network Error"))

        val results = repository.getDetailTvShow(movieId).toList()

        Assert.assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals(errorMessage, failureResult.error)
    }
}