package com.example.movieapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movieapp.core.data.TvShowRemoteMediator
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import createMockTvResponse
import createMockTvShowEntity
import createMockTvShowNetworkModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class TvShowRemoteMediatorTest {

    private lateinit var databaseDataSource: TvShowDatabaseSource
    private lateinit var networkDataSource: TvShowNetworkDataSource
    private lateinit var remoteMediator: TvShowRemoteMediator
    private lateinit var pagingState: PagingState<Int, TvShowEntity>
    private lateinit var pagingConfig: PagingConfig

    @Before
    fun setup() {
        databaseDataSource = mock()
        networkDataSource = mock()
        remoteMediator = TvShowRemoteMediator(databaseDataSource, networkDataSource, MediaType.TvShow.Popular)
        pagingConfig = PagingConfig(pageSize = 20)
        pagingState = PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )
    }

    @Test
    fun `load - refresh - success - with data`() = runTest {
        val networkTvShows = listOf(createMockTvShowNetworkModel(1), createMockTvShowNetworkModel(2))
        val tvShowResponse = createMockTvResponse(networkTvShows)

        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(tvShowResponse))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load - refresh - network failure`() = runTest {
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Failure("Network Error"))

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load - append - success`() = runTest {
        val networkTvShows = listOf(createMockTvShowNetworkModel(1), createMockTvShowNetworkModel(2))
        val tvShowResponse = createMockTvResponse(networkTvShows)
        val existingTvShows = listOf(createMockTvShowEntity(1, MediaType.TvShow.Popular), createMockTvShowEntity(2, MediaType.TvShow.Popular))

        val page = PagingSource.LoadResult.Page(
            data = existingTvShows,
            prevKey = 1,
            nextKey = 3
        )
        val appendPagingState = PagingState(
            pages = listOf(page),
            anchorPosition = 1,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )

        whenever(databaseDataSource.getRemoteKeyByIdAndMediaType(any(),any())).thenReturn(TvShowRemoteKeyEntity(1, MediaType.TvShow.Popular, 1, 2))
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(tvShowResponse))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.APPEND, appendPagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load - prepend - success`() = runTest {
        val networkTvShows = listOf(createMockTvShowNetworkModel(1), createMockTvShowNetworkModel(2))
        val tvShowResponse = createMockTvResponse(networkTvShows)
        val existingTvShows = listOf(createMockTvShowEntity(1, MediaType.TvShow.Popular), createMockTvShowEntity(2, MediaType.TvShow.Popular))
        val page = PagingSource.LoadResult.Page(
            data = existingTvShows,
            prevKey = 1,
            nextKey = 3
        )
        val prependPagingState = PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )
        whenever(databaseDataSource.getRemoteKeyByIdAndMediaType(any(), any())).thenReturn(TvShowRemoteKeyEntity(1, MediaType.TvShow.Popular, 2, 1))
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(tvShowResponse))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.PREPEND, prependPagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load - refresh - empty response`() = runTest {
        val tvShowResponse = createMockTvResponse(emptyList())

        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(tvShowResponse))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load - http exception`() = runTest {
        val response: Response<*> = mock {
            on { code() }.thenReturn(500) // Set the response code
            on { message() }.thenReturn("Internal Server Error") // Optional: Set the response message
        }

        // Create HttpException with the mocked Response
        val httpException = HttpException(response)
        whenever(networkDataSource.getByMediaType(any(), any())).thenThrow(httpException)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

}