package com.example.movieapp.network.source

import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.network.api.MovieApiService
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.GenreResponse
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MovieNetworkDataSourceTest {

    private lateinit var movieService: MovieApiService
    private lateinit var movieNetworkDataSource: MovieNetworkDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieService = mock(MovieApiService::class.java)
        movieNetworkDataSource = MovieNetworkDataSource(movieService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGenreMovie should return success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = Response.success(GenreResponse(listOf(GenreNetwork(1, "Action"))))

        `when`(movieService.getGenreMovie()).thenReturn(mockResponse)

        // Act
        val result = movieNetworkDataSource.getGenreMovie()

        // Assert
        assert(result is CinemaxResponse.Success) // ✅ Check if result is a success
        assertEquals(1, (result as CinemaxResponse.Success).value.genres?.size) // ✅ Check size
        assertEquals("Action", result.value.genres?.first()?.name) // ✅ Validate genre name
    }

    @Test
    fun `getGenreMovie should return failure when API call fails`() = runTest {
        // Arrange
        val errorJson =
            "{\"status_message\": \"Bad Request\"}".toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<GenreResponse>(400, errorJson)
        `when`(movieService.getGenreMovie()).thenReturn(errorResponse)

        // Act
        val result = movieNetworkDataSource.getGenreMovie()

        // Assert
        assert(result is CinemaxResponse.Failure)
        assertEquals("Bad Request", (result as CinemaxResponse.Failure).error)
    }

    @Test
    fun `getByMediaType should return success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = Response.success(
            MovieResponse(
                1, listOf(
                    MovieNetwork(id = 1, name = "Sonic the Hedgehog 3")
                ), 1, 1
            )
        )
        `when`(movieService.getUpcomingMovie(1)).thenReturn(mockResponse)

        // Act
        val result = movieNetworkDataSource.getByMediaType(NetworkMediaType.Movie.UPCOMING, 1)

        // Assert
        assert(result is CinemaxResponse.Success)
        assertEquals(1, (result as CinemaxResponse.Success).value.results?.size) // ✅ Check size
    }

    @Test
    fun `getByMediaType should return failure when API call fails`() = runTest {
        // Arrange
        val errorJson =
            "{\"status_message\": \"Bad Request\"}".toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<MovieResponse>(400, errorJson)
        `when`(movieService.getUpcomingMovie()).thenReturn(errorResponse)

        // Act
        val result = movieNetworkDataSource.getByMediaType(NetworkMediaType.Movie.UPCOMING, 1)

        // Assert
        assert(result is CinemaxResponse.Failure)
        assertEquals("Bad Request", (result as CinemaxResponse.Failure).error)
    }

    @Test
    fun `search should return success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = Response.success(
            MovieResponse(
                1, listOf(
                    MovieNetwork(id = 1, name = "Avengers")
                ), 1, 1
            )
        )
        `when`(movieService.getSearchAll("Avengers", 1)).thenReturn(mockResponse)

        // Act
        val result = movieNetworkDataSource.search("Avengers", 1)

        // Assert
        assert(result is CinemaxResponse.Success)
        assertEquals(1, (result as CinemaxResponse.Success).value.results?.size)
        assertEquals("Avengers", result.value.results?.first()?.name)
    }

    @Test
    fun `getDetailMovie should return success when API call is successful`() = runTest {
        // Arrange
        val movieId = 1
        val mockMovieDetailNetwork = MovieDetailNetwork(
            id = movieId,
            adult = false,
            title = "Sample Movie",
            overview = "This is a sample overview",
        )

        val mockResponse = Response.success(mockMovieDetailNetwork)

        `when`(movieService.getDetailsById(movieId)).thenReturn(mockResponse)

        // Act
        val result = movieNetworkDataSource.getDetailMovie(movieId)
        val actualData = (result as CinemaxResponse.Success).value

        assert(true)
        assertEquals(mockMovieDetailNetwork, actualData)
    }
}