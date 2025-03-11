package com.example.movieapp.network.repository

import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.asMovieEntity
import com.example.movieapp.core.data.asMovieModel
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.data.asTvShowEntity
import com.example.movieapp.core.data.asTvShowModel
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.network.Constants.PAGE_SIZE
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.repository.MovieRepositoryImpl
import com.example.movieapp.core.network.repository.TvShowRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq

class TvShowRepositoryImplTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule() // Handles coroutines in tests

    private lateinit var repository: TvShowRepositoryImpl
    private lateinit var databaseDataSource: TvShowDatabaseSource
    private lateinit var networkDataSource: TvShowNetworkDataSource

    @Before
    fun setup() {
        databaseDataSource = Mockito.mock(TvShowDatabaseSource::class.java)
        networkDataSource = Mockito.mock(TvShowNetworkDataSource::class.java)
        repository = TvShowRepositoryImpl(databaseDataSource, networkDataSource)
    }
    @Test
    fun `networkBoundResourceTvShow - Success - Fetches from network and saves to DB`() = runTest {
        // Arrange

        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val tvs = listOf(createMockTvShowModel(1), createMockTvShowModel(2))
        val networkTvs = listOf(createMockTvShowNetworkModel(1),createMockTvShowNetworkModel(2))
        val movieResponse = createMockTvResponse(networkTvs)

        // Mock database query (initially returns empty list)
        `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(emptyList()))

        // Mock network fetch
        `when`(networkDataSource.getByMediaType(networkType)).thenReturn(CinemaxResponse.Success(movieResponse))

        // Act
        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asTvShowModel() } } },
            fetch = { networkDataSource.getByMediaType(networkType) },
            saveFetchResult = { response ->
                val result = response.results ?: emptyList()
                val entitiesToSave = result.map { it.asTvShowEntity(mediaType) }
                databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType, entitiesToSave)

                // 3. **CRUCIAL:** Update the mock *after* saving!
                `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE))
                    .thenReturn(flowOf(entitiesToSave))
            },
            shouldFetch = { true }
        )

        // Collect results
        val results = resultFlow.toList()

        // Assert
        assertEquals(3, results.size) // Loading, Loading, Success

        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(tvs, successResult.value) // Check if the final data is correct

        // Verify interactions
        Mockito.verify(databaseDataSource, Mockito.times(2)).getByMediaType(mediaType, PAGE_SIZE) // Called multiple times
        Mockito.verify(networkDataSource).getByMediaType(networkType) // Called once
        Mockito.verify(databaseDataSource)
            .deleteByMediaTypeAndInsertAll(eq(mediaType), ArgumentMatchers.anyList()) // Verify data saving
    }

    @Test
    fun `networkBoundResourceTvShow - Network Failure - Returns cached data`() = runTest {
        // Arrange
        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val cachedTvs = listOf(createMockTvShowModel(1))
        val cachedEntities = listOf(createMockTvShowEntity(1, mediaType))

        // Mock database query (returns cached data)
        `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(cachedEntities))

        // Mock network fetch (returns failure)
        `when`(networkDataSource.getByMediaType(mediaType.asNetworkMediaType())).thenReturn(CinemaxResponse.Failure("Network error"))

        // Act
        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asTvShowModel() } } },
            fetch = { networkDataSource.getByMediaType(mediaType.asNetworkMediaType()) },
            saveFetchResult = { /* Not called in case of failure */ },
            shouldFetch = {true}
        )

        // Collect results
        val results = resultFlow.toList()

        // Assert
        assertEquals(3, results.size)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals("Network error", failureResult.error)
        assertEquals(cachedTvs, failureResult.data) // Corrected assertion

        // Verify interactions

        Mockito.verify(networkDataSource).getByMediaType(mediaType.asNetworkMediaType())
        Mockito.verify(databaseDataSource, Mockito.times(2)).getByMediaType(mediaType, PAGE_SIZE) //important, check this line
        Mockito.verify(databaseDataSource, Mockito.never())
            .deleteByMediaTypeAndInsertAll(eq(mediaType), ArgumentMatchers.anyList())
    }

}