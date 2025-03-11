package com.example.movieapp.database.source

import androidx.paging.PagingSource
import com.example.movieapp.core.database.dao.tv.TvShowDao
import com.example.movieapp.core.database.dao.tv.TvShowRemoteKeyDao
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class TvShowDatabaseSourceTest {

    // Mock dependencies
    private lateinit var tvShowDao: TvShowDao
    private lateinit var tvShowRemoteKeyDao: TvShowRemoteKeyDao
    private lateinit var transactionProvider: FakeTransactionProvider

    // Class under test
    private lateinit var tvShowDatabaseSource: TvShowDatabaseSource

    @Before
    fun setUp() {
        // Initialize mocks
        tvShowDao = mock(TvShowDao::class.java)
        tvShowRemoteKeyDao = mock(TvShowRemoteKeyDao::class.java)
        transactionProvider = mock(FakeTransactionProvider::class.java)

        // Initialize the class under test
        tvShowDatabaseSource = TvShowDatabaseSource(
            tvShowDao,
            tvShowRemoteKeyDao,
            transactionProvider
        )
    }

    @After
    fun tearDown() {
        // Optional: Reset mocks after each test
        Mockito.reset(tvShowDao, tvShowRemoteKeyDao, transactionProvider)
    }

    @Test
    fun `getByMediaType should return correct TV shows`() = runTest() {
        // Arrange
        val tvShows = listOf(
            TvShowEntity(
                id = 0, mediaType = MediaType.TvShow.Popular, networkId = 93405,
                name = "Squid Game", overview = "", popularity = 0.0, firstAirDate = "",
                genres = listOf(), originalName = "오징어 게임", originalLanguage = "ko",
                originCountry = listOf(), voteAverage = 0.0, voteCount = 0, posterPath = "",
                backdropPath = "", runtime = 0, rating = 0.0
            ),
            TvShowEntity(
                id = 1 , mediaType = MediaType.TvShow.Popular, networkId = 1399,
                name = "Game of Thrones", overview = "", popularity = 0.0,
                firstAirDate = "", genres = listOf(), originalName = "Game of Thrones",
                originalLanguage = "en", originCountry = listOf(), voteAverage = 0.0,
                voteCount = 0, posterPath = "", backdropPath = "", runtime = 0, rating = 0.0
            ),
        )
        tvShowDao.insertAll(tvShows)
        whenever(tvShowDao.getByMediaType(any(), any())).thenReturn(flowOf(tvShows))

        // Act
        val result = tvShowDatabaseSource.getByMediaType(MediaType.TvShow.Popular, 10).first()

        // Assert
        assertEquals(tvShows, result)
        assertEquals(2, tvShows.size)
        assertEquals(2, result.size)
    }

    @Test
    fun `getPagingByMediaType should return PagingSource from tvShowDao`() {
        // Given
        val pagingSource = mock(PagingSource::class.java) as PagingSource<Int, TvShowEntity>
        whenever(tvShowDao.getPagingByMediaType(any())).thenReturn(pagingSource)

        // When
        val result = tvShowDatabaseSource.getPagingByMediaType(MediaType.TvShow.Popular)

        // Then
        assertEquals(pagingSource, result)
    }

    @Test
    fun `deleteByMediaTypeAndInsertAll should delete and insert within a transaction`() = runTest {
        // Arrange
        val mediaType = MediaType.TvShow.Popular
        val tvShows = listOf(
            TvShowEntity(
                id = 0, mediaType = MediaType.TvShow.Popular, networkId = 93405,
                name = "Squid Game", overview = "", popularity = 0.0, firstAirDate = "",
                genres = listOf(), originalName = "오징어 게임", originalLanguage = "ko",
                originCountry = listOf(), voteAverage = 0.0, voteCount = 0, posterPath = "",
                backdropPath = "", runtime = 0, rating = 0.0
            ),
            TvShowEntity(
                id = 1 , mediaType = MediaType.TvShow.Popular, networkId = 1399,
                name = "Game of Thrones", overview = "", popularity = 0.0,
                firstAirDate = "", genres = listOf(), originalName = "Game of Thrones",
                originalLanguage = "en", originCountry = listOf(), voteAverage = 0.0,
                voteCount = 0, posterPath = "", backdropPath = "", runtime = 0, rating = 0.0
            ),
        )

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        tvShowDatabaseSource.deleteByMediaTypeAndInsertAll(mediaType, tvShows)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(tvShowDao).deleteByMediaType(mediaType)
        verify(tvShowDao).insertAll(tvShows)
    }

    @Test
    fun `getRemoteKeyByIdAndMediaType should return correct remote key`() = runTest() {
        // Arrange
        val remoteKey = TvShowRemoteKeyEntity(1, MediaType.TvShow.Popular, 1, 2)
        tvShowRemoteKeyDao.insertAll(listOf(remoteKey))

        // Stub behavior: When getByIdAndMediaType(1, MediaType.TvShow.Popular) is called, return remoteKey
        whenever(tvShowRemoteKeyDao.getByIdAndMediaType(1, MediaType.TvShow.Popular)).thenReturn(remoteKey)

        // Act
        val result = tvShowDatabaseSource.getRemoteKeyByIdAndMediaType(1, MediaType.TvShow.Popular)

        // Assert
        assertEquals(remoteKey, result)
    }

    @Test
    fun `handlePaging should delete and insert when shouldDeleteTvShowAndRemoteKeys is true`() = runTest {
        // Arrange
        val mediaType = MediaType.TvShow.Popular
        val tvShows = listOf(
            TvShowEntity(
                id = 0, mediaType = MediaType.TvShow.Popular, networkId = 93405,
                name = "Squid Game", overview = "", popularity = 0.0, firstAirDate = "",
                genres = listOf(), originalName = "오징어 게임", originalLanguage = "ko",
                originCountry = listOf(), voteAverage = 0.0, voteCount = 0, posterPath = "",
                backdropPath = "", runtime = 0, rating = 0.0
            ),
            TvShowEntity(
                id = 1 , mediaType = MediaType.TvShow.Popular, networkId = 1399,
                name = "Game of Thrones", overview = "", popularity = 0.0,
                firstAirDate = "", genres = listOf(), originalName = "Game of Thrones",
                originalLanguage = "en", originCountry = listOf(), voteAverage = 0.0,
                voteCount = 0, posterPath = "", backdropPath = "", runtime = 0, rating = 0.0
            ),
        )
        val remoteKeys = listOf(TvShowRemoteKeyEntity(id = 1, mediaType = mediaType,1,2))

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        tvShowDatabaseSource.handlePaging(mediaType, tvShows, remoteKeys, shouldDeleteMoviesAndRemoteKeys = true)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(tvShowDao, times(1)).deleteByMediaType(mediaType)
        verify(tvShowRemoteKeyDao, times(1)).deleteByMediaType(mediaType)
        verify(tvShowRemoteKeyDao, times(1)).insertAll(remoteKeys)
        verify(tvShowDao, times(1)).insertAll(tvShows)
    }

    @Test
    fun `handlePaging should only insert when shouldDeleteTvShowAndRemoteKeys is false`() = runTest {
        // Arrange
        val mediaType = MediaType.TvShow.Popular
        val tvShows = listOf(
            TvShowEntity(
                id = 0, mediaType = MediaType.TvShow.Popular, networkId = 93405,
                name = "Squid Game", overview = "", popularity = 0.0, firstAirDate = "",
                genres = listOf(), originalName = "오징어 게임", originalLanguage = "ko",
                originCountry = listOf(), voteAverage = 0.0, voteCount = 0, posterPath = "",
                backdropPath = "", runtime = 0, rating = 0.0
            ),
            TvShowEntity(
                id = 1 , mediaType = MediaType.TvShow.Popular, networkId = 1399,
                name = "Game of Thrones", overview = "", popularity = 0.0,
                firstAirDate = "", genres = listOf(), originalName = "Game of Thrones",
                originalLanguage = "en", originCountry = listOf(), voteAverage = 0.0,
                voteCount = 0, posterPath = "", backdropPath = "", runtime = 0, rating = 0.0
            ),
        )
        val remoteKeys = listOf(TvShowRemoteKeyEntity(id = 1, mediaType = mediaType,1,2))


        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        tvShowDatabaseSource.handlePaging(mediaType, tvShows, remoteKeys, shouldDeleteMoviesAndRemoteKeys = false)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(tvShowDao, never()).deleteByMediaType(mediaType)
        verify(tvShowRemoteKeyDao, never()).deleteByMediaType(mediaType)
        verify(tvShowRemoteKeyDao, times(1)).insertAll(remoteKeys)
        verify(tvShowDao, times(1)).insertAll(tvShows)
    }

}