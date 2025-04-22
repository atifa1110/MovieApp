package com.example.movieapp.database.source

import com.example.movieapp.core.database.dao.tv.TvShowDetailsDao
import com.example.movieapp.core.database.model.detailMovie.CreditsEntity
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.database.source.TvShowDetailsDatabaseSource
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class TvShowDetailsDatabaseSourceTest {

    private lateinit var tvShowDetailsDao: TvShowDetailsDao
    private lateinit var transactionProvider: DatabaseTransactionProvider
    private lateinit var tvShowDetailsDatabaseSource: TvShowDetailsDatabaseSource

    @Before
    fun setUp() {
        // Mock dependencies
        tvShowDetailsDao = mock(TvShowDetailsDao::class.java)
        transactionProvider = mock(DatabaseTransactionProvider::class.java)

        // Initialize class under test
        tvShowDetailsDatabaseSource = TvShowDetailsDatabaseSource(
            tvShowDetailsDao = tvShowDetailsDao,
            transactionProvider = transactionProvider
        )
    }

    @Test
    fun `getById should return correct tv show details`() = runTest {
        // Arrange
        val tvShowDetails = TvShowDetailsEntity(
            id = 1, name = "Squid Game", adult = false, backdropPath = "",
            episodeRunTime = listOf(), firstAirDate = "", genres = listOf(),
            seasons =  listOf(), homepage = "", inProduction = false,
            languages = listOf(), lastAirDate = "", numberOfEpisodes = 0,
            numberOfSeasons = 0, originCountry = listOf(), originalLanguage = "",
            originalName = "오징어 게임", overview = "", popularity = 0.0,
            posterPath = "", status =  "Returning Series", tagline = "Prepare for the final game.",
            type = "", voteAverage = 0.0, voteCount = 0, credits = CreditsEntity(listOf(), listOf()),
            rating = 0.0
        )

        whenever(tvShowDetailsDao.getById(1)).thenReturn(flowOf(tvShowDetails))

        // Act
        val result = tvShowDetailsDatabaseSource.getById(1).first()

        // Assert
        assertEquals(tvShowDetails, result) // Ensure the returned result matches
    }

    @Test
    fun `deleteAndInsert should delete and insert correctly`() = runTest {
        // Arrange
        val tvShowDetails = TvShowDetailsEntity(
            id = 1, name = "Squid Game", adult = false, backdropPath = "",
            episodeRunTime = listOf(), firstAirDate = "", genres = listOf(),
            seasons =  listOf(), homepage = "", inProduction = false,
            languages = listOf(), lastAirDate = "", numberOfEpisodes = 0,
            numberOfSeasons = 0, originCountry = listOf(), originalLanguage = "",
            originalName = "오징어 게임", overview = "", popularity = 0.0,
            posterPath = "", status =  "Returning Series", tagline = "Prepare for the final game.",
            type = "", voteAverage = 0.0, voteCount = 0, credits = CreditsEntity(listOf(), listOf()),
            rating = 0.0
        )

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        tvShowDetailsDatabaseSource.deleteAndInsert(tvShowDetails)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(tvShowDetailsDao).deleteById(tvShowDetails.id) // Ensure delete was called
        verify(tvShowDetailsDao).insert(tvShowDetails) // Ensure insert was called after delete
    }
}