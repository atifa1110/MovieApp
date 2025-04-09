import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.data.SearchPagingSource
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.asMovieEntity
import com.example.movieapp.core.data.asMovieModel
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.PAGE_SIZE
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.repository.MovieRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.GenreResponse
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.utils.networkBoundResource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
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
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MovieRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var repository: MovieRepositoryImpl
    private var databaseDataSource: MovieDatabaseSource = mockk()
    private var networkDataSource: MovieNetworkDataSource = mockk()

    @Before
    fun setup() {
        repository = MovieRepositoryImpl(databaseDataSource, networkDataSource)
    }

    @Test
    fun `genreMovie - Returns cached data and then fetches and updates`() = runTest {

        val mockGenreEntity = listOf(
            GenreEntity(0,"Action"), GenreEntity(1,"Adventure")
        )
        val mockResponse = GenreResponse(
            listOf(
                GenreNetwork(0,"Action"), GenreNetwork(1,"Adventure")
            )
        )

        val mockGenreModel = listOf(GenreModel(0,"Action"),GenreModel(1,"Adventure"))

        // Arrange (Cached data exists)
        coEvery { databaseDataSource.getGenreMovie() } returns flowOf(emptyList())
        coEvery { networkDataSource.getGenreMovie() } returns CinemaxResponse.Success(mockResponse)
        coEvery { databaseDataSource.insertGenre(any()) } returns Unit
        coEvery { databaseDataSource.getGenreMovie() } returns flowOf(mockGenreEntity)

        // Act
        val results = repository.genreMovie().toList()

        // Assert (Initial cached data)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(mockGenreModel, successResult.value) // Check if the final data is correct
    }

    @Test
    fun `genreMovie - Returns error if network fetch fails`() = runTest {
        val errorMessage = "Network Error"
        // Arrange (Cache is empty)
        coEvery { databaseDataSource.getGenreMovie() } returns flowOf(emptyList())
        coEvery { networkDataSource.getGenreMovie() } returns CinemaxResponse.Failure(errorMessage)

        // Act
        val results = repository.genreMovie().toList()

        // Assert (Only error)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals(errorMessage, failureResult.error)
    }

    @Test
    fun `searchMovie - Success - Returns PagingData`() = runBlocking {
        // Arrange
        val query = "title"
        val movie1 = createMockMovieNetworkModelType(1)
        val movie2 = createMockMovieNetworkModelType(2)
        val movieDetail1 = createMockDetailMovieModel(1,120)
        val movieDetail2 = createMockDetailMovieModel(2,130)
        val mockResponse = createMockMovieResponse(listOf(movie1, movie2))
        val mockResponsePage2 = createMockMovieResponse(emptyList())

        coEvery { networkDataSource.search(page = 1, query = query) } returns CinemaxResponse.Success(mockResponse)
        coEvery { networkDataSource.search(page = 2, query = query) } returns CinemaxResponse.Success(mockResponsePage2)
        coEvery { networkDataSource.getDetailMovie(1) } returns CinemaxResponse.Success(movieDetail1)
        coEvery { networkDataSource.getDetailMovie(2) } returns CinemaxResponse.Success(movieDetail2)

        // Collect the PagingData and verify its content
        val pagingData = repository.searchMovie(query).asSnapshot()

        assertEquals(2, pagingData.size)
        assertEquals("Title 1", pagingData[0].title)
        assertEquals("Title 2", pagingData[1].title)
    }

    @Test
    fun `searchMovie - Failure - Returns LoadResult Error`() = runTest {
        // Arrange
        val query = "test"
        val errorMessage = "Network error"
        coEvery { networkDataSource.search(page = 1, query = query) } returns CinemaxResponse.Failure(errorMessage)

        // Act
        val result = repository.searchMovie(query)

        // Act
        try {
            result.asSnapshot()
            // If asSnapshot() doesn't throw, the test should fail
            assertTrue("Expected asSnapshot() to throw an exception", false)
        } catch (e: Exception) {
            // Assert that the exception thrown contains the expected error message
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun `searchMovie - IOException - asSnapshot throws IOException`() = runTest {
        // Arrange
        val query = "test"
        val errorMessage = "Simulated IO error"
        coEvery { networkDataSource.search(page = 1, query = query) } throws IOException(errorMessage)

        // Act
        try {
            repository.searchMovie(query).asSnapshot()
            assertTrue("Expected IOException to be thrown", false) // Fail if no exception
        } catch (e: IOException) {
            // Assert that the caught exception has the expected message
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun `searchMovie - HttpException - asSnapshot throws HttpException`() = runTest {
        // Arrange
        val query = "test"
        val errorCode = 404
        val errorMessage = "Not Found"
        val response = Response.error<MovieResponse>(errorCode, errorMessage.toResponseBody())
        coEvery { networkDataSource.search(page = 1, query = query) } throws HttpException(response)

        // Act
        try {
            repository.searchMovie(query).asSnapshot()
            assertTrue("Expected HttpException to be thrown", false) // Fail if no exception
        } catch (e: HttpException) {
            // Assert that the caught exception has the expected code and message
            assertEquals(errorCode, e.code())
            assertEquals(errorMessage,e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun `searchMovie - getDetailMovie Failure - Returns movie with default runtime`() = runBlocking {
        // Arrange
        val query = "title"
        val movie1 = createMockMovieNetworkModelType(1)
        val movie2 = createMockMovieNetworkModelType(2)
        val mockResponse = createMockMovieResponse(listOf(movie1, movie2))
        val mockResponsePage2 = createMockMovieResponse(emptyList())

        coEvery { networkDataSource.search(page = 1, query = query) } returns CinemaxResponse.Success(mockResponse)
        coEvery { networkDataSource.search(page = 2, query = query) } returns CinemaxResponse.Success(mockResponsePage2)
        coEvery { networkDataSource.getDetailMovie(1) } returns CinemaxResponse.Failure("Network Error")
        coEvery { networkDataSource.getDetailMovie(2) } returns CinemaxResponse.Failure("Network Error")

        // Collect the PagingData and verify its content
        val pagingData = repository.searchMovie(query).asSnapshot()

        assertEquals(2, pagingData.size)
        assertEquals("Title 1", pagingData[0].title)
        assertEquals("Title 2", pagingData[1].title)
        assertEquals(0, pagingData[0].runtime)
        assertEquals(0, pagingData[1].runtime)
    }

    @Test
    fun `getByMediaType - Movie - Returns cached data and then fetches and updates`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()

        val moviesEntity = listOf(createMockMovieEntity(1,mediaTypeModel), createMockMovieEntity(2,mediaTypeModel))
        val moviesModel = listOf(MovieModel(
            id = 1, title = "Title 1", overview = "Overview 1",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 1", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0
        ), MovieModel(
            id = 2, title = "Title 2", overview = "Overview 2",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 2", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0))
        val networkMovies = listOf(createMockMovieNetworkModel(1),createMockMovieNetworkModel(2))
        val movieResponse = createMockMovieResponse(networkMovies)

        // Arrange (Cached data exists)
        coEvery { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE) } returns flowOf(emptyList())
        coEvery { networkDataSource.getByMediaType(networkType) } returns CinemaxResponse.Success(movieResponse)
        coEvery { databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType, any()) } returns Unit
        coEvery { databaseDataSource.getByMediaType(mediaType, PAGE_SIZE) } returns flowOf(moviesEntity)

        // Act
        val results = repository.getByMediaType(mediaTypeModel).toList()

        // Assert (Initial cached data)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(moviesModel, successResult.value) // Check if the final data is correct

    }

    @Test
    fun `getByMediaType - Movie - Network Failure - Returns only error if no cache`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
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
    fun `getByMediaTypeGenre - Movie - Returns cached data and then fetches and updates`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()

        val movies = listOf(MovieModel(
            id = 1, title = "Title 1", overview = "Overview 1",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 1", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0
        ), MovieModel(
            id = 2, title = "Title 2", overview = "Overview 2",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 2", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0
        ))
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

        // Arrange (Cached data exists)
        coEvery { databaseDataSource.getMoviesWithGenres(mediaType, PAGE_SIZE) } returns flowOf(movieWithGenres)
        coEvery { networkDataSource.getByMediaType(networkType) } returns CinemaxResponse.Success(movieResponse)
        coEvery { databaseDataSource.deleteByMediaTypeAndInsertAll(mediaType, any()) } returns Unit

        // Act
        val results = repository.getByMediaTypeGenre(mediaTypeModel).toList()

        // Assert (Initial cached data)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(movies, successResult.value)
    }


    @Test
    fun `getByMediaTypeGenre - Movie - Returns error if network fetch fails`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()

        // Arrange (Cache is empty)
        coEvery { databaseDataSource.getMoviesWithGenres(mediaType, PAGE_SIZE) } returns flowOf(emptyList())
        val errorMessage = "Network Error"
        coEvery { networkDataSource.getByMediaType(networkType) } returns CinemaxResponse.Failure(errorMessage)

        // Act
        val results = repository.getByMediaTypeGenre(mediaTypeModel).toList()

        // Assert (Only error)
        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals(errorMessage, failureResult.error)

    }

    @Test
    fun `getPagingByMediaType - Returns data from PagingSource`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val mediaType = mediaTypeModel.asMediaType()
        val networkType = mediaType.asNetworkMediaType()
        // Arrange (Database has initial data)
        val moviesEntity = listOf(createMockMovieEntity(1, mediaTypeModel), createMockMovieEntity(2, mediaTypeModel))

        val movies = listOf(MovieModel(
            id = 1, title = "Title 1", overview = "Overview 1",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 1", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0
        ), MovieModel(
            id = 2, title = "Title 2", overview = "Overview 2",
            posterPath = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "October 26, 2023" , voteAverage = 8.0, runtime = 0, mediaType = "movie_popular",
            originalTitle = "Title 2", originalLanguage = "en", voteCount = 100,
            popularity = 7.5, rating = 4.0
        ))

        val movie1 = createMockMovieNetworkModelType(1)
        val movie2 = createMockMovieNetworkModelType(2)
        val mockResponse = createMockMovieResponse(listOf(movie1, movie2))
        val movieDetail1 = createMockDetailMovieModel(1, 120)
        val movieDetail2 = createMockDetailMovieModel(2, 130)

        coEvery { networkDataSource.getByMediaType(networkType, 1) } returns CinemaxResponse.Success(mockResponse)
        coEvery { networkDataSource.getDetailMovie(1) } returns CinemaxResponse.Success(movieDetail1)
        coEvery { networkDataSource.getDetailMovie(2) } returns CinemaxResponse.Success(movieDetail2)
        coEvery { databaseDataSource.handlePaging(any(), any(), any(), any()) } returns Unit
        // Mock remote key lookups (even if only one page is loaded)
        coEvery { databaseDataSource.getRemoteKeyByIdAndMediaType(1, mediaType) } returns mockk<MovieRemoteKeyEntity> {
            every { prevPage } returns null // Define behavior for prevPage
            every { nextPage } returns 2
        }
        coEvery { databaseDataSource.getRemoteKeyByIdAndMediaType(2, mediaType) } returns mockk<MovieRemoteKeyEntity> {
            every { prevPage } returns 1 // Define behavior for prevPage
            every { nextPage } returns null
        }

        val mockPagingSource = mockk<PagingSource<Int, MovieEntity>>()

        // Define the behavior of its load() method
        coEvery { mockPagingSource.load(any()) } returns PagingSource.LoadResult.Page(
            data = moviesEntity,
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
        assertEquals(movies,result)
    }

    @Test
    fun `getPagingByMediaType - Initial Refresh Network Error`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
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


