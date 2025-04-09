package com.example.movieapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movieapp.core.data.MovieRemoteMediator
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.network.PAGE_SIZE
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import createMockDetailMovieModel
import createMockMovieEntity
import createMockMovieNetworkModel
import createMockMovieResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class MovieRemoteMediatorTest {

    private lateinit var databaseDataSource: MovieDatabaseSource
    private lateinit var networkDataSource: MovieNetworkDataSource
    private lateinit var remoteMediator: MovieRemoteMediator
    private lateinit var pagingState: PagingState<Int, MovieEntity>
    private lateinit var pagingConfig: PagingConfig

    @Before
    fun setup() {
        databaseDataSource = mock()
        networkDataSource = mock()
        remoteMediator = MovieRemoteMediator(databaseDataSource, networkDataSource, MediaType.Movie.Popular)
        pagingConfig = PagingConfig(pageSize = 20)
        pagingState = PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = mock(),
            leadingPlaceholderCount = 0
        )
    }

    @Test
    fun `load - refresh - success - check movie entities`() = runTest {
        val networkMovies = listOf(createMockMovieNetworkModel(1), createMockMovieNetworkModel(2))
        val movieResponse = createMockMovieResponse(networkMovies)
        val movieDetail1 = createMockDetailMovieModel(1, 120)
        val movieDetail2 = createMockDetailMovieModel(2, 130)

        val movieEntities = listOf(createMockMovieEntity(1,MediaTypeModel.Movie.Popular).copy(runtime = 120),
                createMockMovieEntity(2,MediaTypeModel.Movie.Popular).copy(runtime = 130))

        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(movieResponse))
        whenever(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Success(movieDetail1))
        whenever(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Success(movieDetail2))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)
        whenever(databaseDataSource.getByMediaType(any(), any())).thenReturn(flowOf(movieEntities))

        val page = PagingSource.LoadResult.Page(
            data = movieEntities,
            prevKey = 1,
            nextKey = 3
        )
        val pagingState = PagingState(
            pages = listOf(page),
            anchorPosition = 1,
            config = mock(),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)

        // Retrieve the movies from the database
        val movies = databaseDataSource.getByMediaType(MediaType.Movie.Popular, PAGE_SIZE).first()

        // Assert the movies
        assertEquals(2, movies.size)
        assertEquals("Title 1", movies[0].title)
        assertEquals(120, movies[0].runtime)
        assertEquals("Title 2", movies[1].title)
        assertEquals(130, movies[1].runtime)
    }

    @Test
    fun `load - refresh - network failure`() = runTest {
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(
            CinemaxResponse.Failure("Network Error")
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assert(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load - refresh - detail movie failure`() = runTest {
        val networkMovies = listOf(createMockMovieNetworkModel(1), createMockMovieNetworkModel(2))
        val movieResponse = createMockMovieResponse(networkMovies)
        val movieDetail1 = createMockDetailMovieModel(1, 120)

        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(movieResponse))
        whenever(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Success(movieDetail1))
        whenever(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Failure("Detail Error"))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load - append - success`() = runTest {
        val networkMovies = listOf(createMockMovieNetworkModel(3), createMockMovieNetworkModel(4))
        val movieResponse = createMockMovieResponse(networkMovies)
        val movieDetail3 = createMockDetailMovieModel(3, 100)
        val movieDetail4 = createMockDetailMovieModel(4, 110)

        val movieEntities = listOf(createMockMovieEntity(1,MediaTypeModel.Movie.Popular).copy(runtime = 120),
            createMockMovieEntity(2,MediaTypeModel.Movie.Popular).copy(runtime = 130))


        val page = PagingSource.LoadResult.Page(
            data = movieEntities,
            prevKey = 1,
            nextKey = 3
        )
        val appendPagingState = PagingState(
            pages = listOf(page),
            anchorPosition = 1,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )

        whenever(databaseDataSource.getRemoteKeyByIdAndMediaType(any(), any())).thenReturn(MovieRemoteKeyEntity(1, MediaType.Movie.Popular, 1, 2))
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(movieResponse))
        whenever(networkDataSource.getDetailMovie(3)).thenReturn(CinemaxResponse.Success(movieDetail3))
        whenever(networkDataSource.getDetailMovie(4)).thenReturn(CinemaxResponse.Success(movieDetail4))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.APPEND, appendPagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load - prepend - success`() = runTest {
        val networkMovies = listOf(createMockMovieNetworkModel(5), createMockMovieNetworkModel(6))
        val movieResponse = createMockMovieResponse(networkMovies)
        val movieDetail5 = createMockDetailMovieModel(5, 130)
        val movieDetail6 = createMockDetailMovieModel(6, 140)

        val movieEntities = listOf(createMockMovieEntity(1,MediaTypeModel.Movie.Popular).copy(runtime = 120),
            createMockMovieEntity(2,MediaTypeModel.Movie.Popular).copy(runtime = 130))

        val page = PagingSource.LoadResult.Page(
            data = movieEntities,
            prevKey = 1,
            nextKey = 3
        )
        val prependPagingState = PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )

        whenever(databaseDataSource.getRemoteKeyByIdAndMediaType(any(), any())).thenReturn(
            MovieRemoteKeyEntity(3, MediaType.Movie.Popular, 2, 1))
        whenever(networkDataSource.getByMediaType(any(), any())).thenReturn(CinemaxResponse.Success(movieResponse))
        whenever(networkDataSource.getDetailMovie(5)).thenReturn(CinemaxResponse.Success(movieDetail5))
        whenever(networkDataSource.getDetailMovie(6)).thenReturn(CinemaxResponse.Success(movieDetail6))
        whenever(databaseDataSource.handlePaging(any(), any(), any(), any())).thenReturn(Unit)

        val result = remoteMediator.load(LoadType.PREPEND, prependPagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
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