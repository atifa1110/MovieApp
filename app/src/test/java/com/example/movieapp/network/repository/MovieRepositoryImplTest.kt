package com.example.movieapp.network.repository

import android.util.Log
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.testing.*
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.data.MovieRemoteMediator
import com.example.movieapp.core.data.SearchPagingSource
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.asMovieEntity
import com.example.movieapp.core.data.asMovieModel
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.defaultPagingConfig
import com.example.movieapp.core.network.repository.MovieRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.GenreResponse
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.ui.pagingMap
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MovieRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule() // Handles coroutines in tests

    private lateinit var repository: MovieRepositoryImpl
    private lateinit var databaseDataSource: MovieDatabaseSource
    private lateinit var networkDataSource: MovieNetworkDataSource

    @Before
    fun setup() {
        databaseDataSource = mock(MovieDatabaseSource::class.java)
        networkDataSource = mock(MovieNetworkDataSource::class.java)
        repository = MovieRepositoryImpl(databaseDataSource, networkDataSource)
    }

    @Test
    fun `searchMovie - Success - Returns PagingData`() = runTest {
        // Arrange
        val query = "test"
        val movie1 = createMockMovieNetworkModel(id = 1)
        val movie2 = createMockMovieNetworkModel(id = 2)
        val movieDetail1 = createMockDetailMovieModel(1,120)
        val movieDetail2 = createMockDetailMovieModel(2,130)

        val mockResponse = createMockMovieResponse(listOf(movie1, movie2))

        `when`(networkDataSource.search(page = 1, query = query)).thenReturn(CinemaxResponse.Success(mockResponse))

        `when`(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Success(movieDetail1))
        `when`(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Success(movieDetail2))

        val pagingSource = SearchPagingSource(query, networkDataSource)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // Assert
        val expected = PagingSource.LoadResult.Page(
            data = listOf(
                createMockMovieModel(1).copy(runtime = 120),
                createMockMovieModel(2).copy(runtime = 130)
            ),
            prevKey = null,
            nextKey = 2 // Because totalPages in mockResponse is 1
        )
        assertEquals(expected, result)
        verify(networkDataSource).search(page = 1, query = query)
    }

    @Test
    fun `searchMovie - Failure - Returns LoadResult Error`() = runTest {
        // Arrange
        val query = "test"
        val errorMessage = "Network error"
        `when`(networkDataSource.search(page = 1, query = query))
            .thenReturn(CinemaxResponse.Failure(errorMessage))

        val pagingSource = SearchPagingSource(query, networkDataSource)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(errorMessage, (result as PagingSource.LoadResult.Error).throwable.message)
        verify(networkDataSource).search(page = 1, query = query)
    }

    @Test
    fun `searchMovie - IOException - Returns LoadResult Error`() = runTest {
        // Arrange
        val query = "test"
        val errorMessage = "IO error" // Create an error message, not an exception
        `when`(networkDataSource.search(page = 1, query = query))
            .thenReturn(CinemaxResponse.Failure(errorMessage)) // Return a Failure

        val pagingSource = SearchPagingSource(query, networkDataSource)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(errorMessage, (result as PagingSource.LoadResult.Error).throwable.message) // Check the message
        verify(networkDataSource).search(page = 1, query = query)
    }

    @Test
    fun `searchMovie - HttpException - Returns LoadResult Error`() = runTest {
        // Arrange
        val query = "test"
        val response = Response.error<MovieResponse>(404, "Not Found".toResponseBody())

        val exception = HttpException(response)
        `when`(networkDataSource.search(page = 1, query = query)).thenThrow(exception)

        val pagingSource = SearchPagingSource(query, networkDataSource)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)
        verify(networkDataSource).search(page = 1, query = query)

    }

    @Test
    fun `searchMovie - getDetailMovie Failure - Returns movie with default runtime`() = runTest {

        val query = "query"
        val movie = createMockMovieNetworkModel(id = 1)
        val mockResponse = createMockMovieResponse(listOf(movie))

        `when`(networkDataSource.search(page = 1, query = query))
            .thenReturn(CinemaxResponse.Success(mockResponse))
        `when`(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Failure("error"))

        val pagingSource = SearchPagingSource(query, networkDataSource)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(createMockMovieModel(1).copy(runtime = 0)), // Default value
            prevKey = null,
            nextKey = 2
        )
        assertEquals(expected, result)
        verify(networkDataSource).search(page = 1, query = query)
        verify(networkDataSource).getDetailMovie(1) //verify getDetailMovie was called.
    }

    private val PAGE_SIZE = 20 // Or whatever your actual page size is

    private fun createMockMovieModel(id: Int): MovieModel {
        return MovieModel(
            id = id,
            title = "Title $id",
            overview = "Overview $id",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" ,
            voteAverage = 8.0,        // Consistent with Network Model
            runtime = 0,
            mediaType = "movie_popular",
            originalTitle = "Title $id",
            originalLanguage = "en",
            voteCount = 100,
            popularity = 7.5,
            rating = 4.0
        )
    }

    private fun createMockMovieEntity(id: Int, mediaType: MediaTypeModel.Movie): MovieEntity { // Changed parameter type
        return MovieEntity(
            id = id,
            title = "Title $id",
            networkId = id,
            overview = "Overview $id",
            adult = false,
            genreIds = emptyList(),
            originalLanguage = "en",
            originalTitle = "Title $id",
            popularity = 7.5,
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            releaseDate = "October 26, 2023",
            voteAverage = 8.0,
            voteCount = 100,
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            runtime = 0,
            video = false,           // Consistent with Network Model
            rating = 4.0,          // Consistent with Network and Movie Model (voteAverage)
            mediaType = mediaType.asMediaType()
        )
    }

    private fun createMockMovieNetworkModel(id: Int): MovieNetwork { // Corrected return type
        return MovieNetwork(
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = emptyList(),
            id = id,
            originalLanguage = "en",
            originalTitle = "Title $id",
            overview = "Overview $id",
            popularity = 7.5,
            posterPath = "/poster.jpg",
            releaseDate = "2023-10-26",
            title = "Title $id",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            mediaType = "movie",
            name = null,
        )
    }

    private fun createMockMovieResponse(movies: List<MovieNetwork>): MovieResponse {
        return MovieResponse(
            page = 1,
            results = movies,
            totalPages = 1,
            totalResults = movies.size
        )
    }

    @Test
    fun `networkBoundResource - Success - Fetches from network and saves to DB`() = runTest {
        // Arrange

        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val movies = listOf(createMockMovieModel(1), createMockMovieModel(2))
        val networkMovies = listOf(createMockMovieNetworkModel(1),createMockMovieNetworkModel(2))
        val movieResponse = createMockMovieResponse(networkMovies)

        // Mock database query (initially returns empty list)
        `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(emptyList()))

        // Mock network fetch
        `when`(networkDataSource.getByMediaType(networkType)).thenReturn(CinemaxResponse.Success(movieResponse))

        // Act
        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asMovieModel() } } },
            fetch = { networkDataSource.getByMediaType(networkType) },
            saveFetchResult = { response ->
                val result = response.results ?: emptyList()
                val entitiesToSave = result.map { it.asMovieEntity(mediaType) }
                databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType, entitiesToSave)

                // 3. **CRUCIAL:** Update the mock *after* saving!
                `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(entitiesToSave))
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
        assertEquals(movies, successResult.value) // Check if the final data is correct

        // Verify interactions
        verify(databaseDataSource, times(2)).getByMediaType(mediaType, PAGE_SIZE) // Called multiple times
        verify(networkDataSource).getByMediaType(networkType) // Called once
        verify(databaseDataSource).deleteByMediaTypeAndInsertAll(eq(mediaType), anyList()) // Verify data saving
    }

    @Test
    fun `networkBoundResource - Network Failure - Returns cached data`() = runTest {
        // Arrange
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val cachedMovies = listOf(createMockMovieModel(1))
        val cachedEntities = listOf(createMockMovieEntity(1, mediaTypeModel))

        // Mock database query (returns cached data)
        `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(cachedEntities))

        // Mock network fetch (returns failure)
        `when`(networkDataSource.getByMediaType(mediaType.asNetworkMediaType())).thenReturn(CinemaxResponse.Failure("Network error"))

        // Act
        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asMovieModel() } } },
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
        assertEquals(cachedMovies, failureResult.data) // Corrected assertion

        // Verify interactions

        verify(networkDataSource).getByMediaType(mediaType.asNetworkMediaType())
        verify(databaseDataSource, times(2)).getByMediaType(mediaType, PAGE_SIZE) //important, check this line
        verify(databaseDataSource, never()).deleteByMediaTypeAndInsertAll(eq(mediaType), anyList())
    }

    @Test
    fun `networkBoundResource getMoviesWithGenres - Success - Fetches from network and saves to DB`() = runTest {

        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val movies = listOf(createMockMovieModel(1), createMockMovieModel(2))
        val networkMovies = listOf(createMockMovieNetworkModel(1),createMockMovieNetworkModel(2))
        val movieResponse = createMockMovieResponse(networkMovies)

        val movieWithGenres = listOf(
            MovieWithGenres(
                movie = createMockMovieEntity(1, mediaTypeModel),
                genreNames = listOf("Action")
            ),
            MovieWithGenres(
                movie = createMockMovieEntity(2, mediaTypeModel),
                genreNames = listOf("Action")
            )
        )

        // Mock database query (returns cached data)
        `when`(databaseDataSource.getMoviesWithGenres(eq(mediaType), any())).thenReturn(
            flowOf(movieWithGenres))

        // Mock network fetch (returns success)
        `when`(networkDataSource.getByMediaType(networkType)).thenReturn(
            CinemaxResponse.Success(movieResponse)
        )

        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getMoviesWithGenres(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asMovieModel() } } },
            fetch = { networkDataSource.getByMediaType(mediaType.asNetworkMediaType()) },
            saveFetchResult = { response ->
                val result = response.results ?: emptyList()
                val entitiesToSave = result.map { it.asMovieEntity(mediaType) }
                databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType, entitiesToSave)

                // 3. **CRUCIAL:** Update the mock *after* saving!
                `when`(databaseDataSource.getByMediaType(mediaType, PAGE_SIZE)).thenReturn(flowOf(entitiesToSave))
            },
            shouldFetch = { true }
        )

        // Collect results
        val results = resultFlow.toList()
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(movies, successResult.value) // Check if the final data is correct

        // Verify interactions
        verify(databaseDataSource, times(2)).getMoviesWithGenres(mediaType, PAGE_SIZE) // Called multiple times
        verify(networkDataSource).getByMediaType(networkType) // Called once
        verify(databaseDataSource).deleteByMediaTypeAndInsertAll(eq(mediaType), anyList()) // Verify data saving

    }

    @Test
    fun `networkBoundResource getMoviesWithGenres - Network Failure - - Returns cached data`() = runTest {

        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        val cachedMovies = listOf(createMockMovieModel(1),createMockMovieModel(2))

        val movieWithGenres = listOf(
            MovieWithGenres(
                movie = createMockMovieEntity(1, mediaTypeModel),
                genreNames = listOf("Action")
            ),
            MovieWithGenres(
                movie = createMockMovieEntity(2, mediaTypeModel),
                genreNames = listOf("Action")
            )
        )

        // Mock database query (returns cached data)
        `when`(databaseDataSource.getMoviesWithGenres(eq(mediaType), any())).thenReturn(
            flowOf(movieWithGenres))

        // Mock network fetch (returns success)
        `when`(networkDataSource.getByMediaType(networkType)).thenReturn(
            CinemaxResponse.Failure("Network error")
        )

        val resultFlow = networkBoundResource(
            query = { databaseDataSource.getMoviesWithGenres(mediaType, PAGE_SIZE).map { it.map { entity -> entity.asMovieModel() } } },
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
        assertEquals(cachedMovies, failureResult.data) // Corrected assertion

        // Verify interactions
        verify(networkDataSource).getByMediaType(mediaType.asNetworkMediaType())
        verify(databaseDataSource, never()).deleteByMediaTypeAndInsertAll(eq(mediaType), anyList())
    }


//    @OptIn(ExperimentalPagingApi::class)
//    @Test
//    fun `getPagingByMediaType should return PagingData of MovieModel`() = runTest {
//        val mediaType = MediaType.Movie.Popular
//        val mediaModel = MediaTypeModel.Movie.Popular
//
//        // 🔹 1. Siapkan data mock untuk API & Database
//        val networkMovies = listOf(createMockMovieNetworkModel(1), createMockMovieNetworkModel(2))
//        val movieResponse = createMockMovieResponse(networkMovies)
//        val fakeMovies = listOf(createMockMovieEntity(1, mediaModel), createMockMovieEntity(2, mediaModel))
//        val cachedMovies = listOf(createMockMovieModel(1), createMockMovieModel(2))
//
//        val fakePagingSource = FakeMoviePagingSource(fakeMovies)
//
//        // 🔹 2. Pastikan API mengembalikan data valid
//        whenever(networkDataSource.getByMediaType(any(), any()))
//            .thenReturn(CinemaxResponse.Success(movieResponse))
//
//        // 🔹 3. Gunakan RemoteMediator yang asli (tidak di-mock!)
//        val remoteMediator = MovieRemoteMediator(databaseDataSource, networkDataSource, mediaType)
//
//        // 🔹 4. Pastikan Database mengembalikan Fake Paging Source
//        whenever(databaseDataSource.getPagingByMediaType(mediaType))
//            .thenReturn(fakePagingSource)
//
//        val result = repository.getPagingByMediaType(mediaModel).first()
//
//        advanceUntilIdle()
//
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = MovieDiffCallback(),
//            updateCallback = NoopListUpdateCallback(),
//            mainDispatcher = Dispatchers.Main,
//            workerDispatcher = Dispatchers.Default
//        )
//
//        differ.submitData(result)
//
//        assertTrue(result is PagingData)
//        assertEquals(2, differ.itemCount)
//    }
//
//
//    fun <T : Any> PagingData<T>.collectDataBlocking(): List<T> = runBlocking {
//        val result = mutableListOf<T>()
//        this@collectDataBlocking.map { result.add(it) }
//        result
//    }
}
