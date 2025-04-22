package com.example.movieapp.network.repository

import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.network.Constants.PAGE_SIZE
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.repository.TvShowRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import createMockTvResponse
import createMockTvShowEntity
import createMockTvShowModel
import createMockTvShowNetworkModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class TvShowRepositoryImplTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule() // Handles coroutines in tests

    private lateinit var repository: TvShowRepositoryImpl
    private var databaseDataSource: TvShowDatabaseSource = mockk()
    private var networkDataSource: TvShowNetworkDataSource = mockk()

    @Before
    fun setup() {
        repository = TvShowRepositoryImpl(databaseDataSource, networkDataSource)
    }


    @Test
    fun `getByMediaType - TvShow - Returns cached data and then fetches and updates`() = runTest {

        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val tvs = listOf(createMockTvShowModel(1), createMockTvShowModel(2))
        val networkTvs = listOf(createMockTvShowNetworkModel(1), createMockTvShowNetworkModel(2))
        val tvResponse = createMockTvResponse(networkTvs)
        val cachedEntities = listOf(createMockTvShowEntity(1, mediaType),createMockTvShowEntity(2, mediaType))

        // Arrange
        coEvery { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE) } returns flowOf(emptyList())
        coEvery { networkDataSource.getByMediaType(networkType) } returns CinemaxResponse.Success(tvResponse)
        coEvery { databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType,any()) } returns Unit
        coEvery { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE) } returns flowOf(cachedEntities)

        // Act
        val results = repository.getByMediaType(mediaTypeModel).toList()

        // Assert (Initial cached data)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(tvs, successResult.value) // Check if the final data is correct
    }

    @Test
    fun `getByMediaType - TvShow - Network Failure - Returns only error if no cache`() = runTest {
        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val errorMessage = "Network Error"

        // Arrange (No cached data, network fetch fails)
        coEvery { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE) } returns flowOf(emptyList())
        coEvery { networkDataSource.getByMediaType(networkType) } returns CinemaxResponse.Failure(errorMessage)

        // Act
        val results = repository.getByMediaType(mediaTypeModel).toList()

        // Assert (Only error)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals(errorMessage, failureResult.error)
    }

    @Test
    fun `getPagingByMediaType - Returns data from PagingSource`() = runTest {
        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()

        // Arrange (Database has initial data)
        val tvsEntity = listOf(createMockTvShowEntity(1, mediaType),createMockTvShowEntity(2, mediaType))
        val tvs = listOf(createMockTvShowModel(1), createMockTvShowModel(2))
        val networkTvs = listOf(createMockTvShowNetworkModel(1), createMockTvShowNetworkModel(2))
        val tvResponse = createMockTvResponse(networkTvs)

        coEvery { networkDataSource.getByMediaType(networkType, 1) } returns CinemaxResponse.Success(tvResponse)
        coEvery { databaseDataSource.handlePaging(any(), any(), any(), any()) } returns Unit
        // Mock remote key lookups (even if only one page is loaded)
        coEvery { databaseDataSource.getRemoteKeyByIdAndMediaType(1, mediaType) } returns mockk<TvShowRemoteKeyEntity> {
            every { prevPage } returns null // Define behavior for prevPage
            every { nextPage } returns 2
        }
        coEvery { databaseDataSource.getRemoteKeyByIdAndMediaType(2, mediaType) } returns mockk<TvShowRemoteKeyEntity> {
            every { prevPage } returns 1 // Define behavior for prevPage
            every { nextPage } returns null
        }

        val mockPagingSource = mockk<PagingSource<Int, TvShowEntity>>()

        // Define the behavior of its load() method
        coEvery { mockPagingSource.load(any()) } returns PagingSource.LoadResult.Page(
            data = tvsEntity,
            prevKey = null,
            nextKey = null
        )

        // **Add this line to handle registerInvalidatedCallback:**
        every { mockPagingSource.registerInvalidatedCallback(any()) } just Runs

        // Tell the databaseDataSource to return this mock PagingSource
        coEvery { databaseDataSource.getPagingByMediaType(mediaType) } returns mockPagingSource

        // Act
        val pagingDataFlow = repository.getPagingByMediaType(mediaTypeModel)
        val result = pagingDataFlow.asSnapshot()

        // Assert
        assertEquals(2, result.size)
        assertEquals(tvs,result)
    }

    @Test
    fun `getPagingByMediaType - Initial Refresh Network Error`() = runTest {
        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val errorMessage = "Network Error"

        // Arrange
        coEvery { networkDataSource.getByMediaType(networkType, 1) } returns CinemaxResponse.Failure(errorMessage)
        coEvery { databaseDataSource.getPagingByMediaType(mediaType) } returns mockk {
            every { invalidate() } returns Unit
            coEvery { load(any()) } returns PagingSource.LoadResult.Page(emptyList(), null, null) // Initial empty data
            every { registerInvalidatedCallback(any()) } just Runs
        }

        // Act
        val pagingDataFlow = repository.getPagingByMediaType(mediaTypeModel)

        try {
            pagingDataFlow.asSnapshot()
            assertTrue("Expected Exception to be thrown", false) // Fail if no exception
        } catch (e: Exception) {
            // Assert that the caught exception has the expected message
            assertEquals(errorMessage, e.message)
        }
    }
}

