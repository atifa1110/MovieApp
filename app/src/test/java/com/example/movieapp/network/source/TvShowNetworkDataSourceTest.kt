package com.example.movieapp.network.source

import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.network.api.TvShowApiService
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.tv.TvShowDetailNetwork
import com.example.movieapp.core.network.response.tv.TvShowNetwork
import com.example.movieapp.core.network.response.tv.TvShowResponse
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class TvShowNetworkDataSourceTest {

    @Mock
    private lateinit var tvShowApiService: TvShowApiService
    private lateinit var tvShowNetworkDataSource: TvShowNetworkDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        tvShowApiService = mock(TvShowApiService::class.java)
        tvShowNetworkDataSource = TvShowNetworkDataSource(tvShowApiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getByMediaType should return success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = Response.success(
            TvShowResponse(
                1, listOf(
                    TvShowNetwork(id = 1, name = "Sonic the Hedgehog 3")
                ), 1, 1
            )
        )
        Mockito.`when`(tvShowApiService.getPopularTv(1)).thenReturn(mockResponse)

        // Act
        val result = tvShowNetworkDataSource.getByMediaType(NetworkMediaType.TvShow.POPULAR, 1)

        // Assert
        assert(result is CinemaxResponse.Success)
        assertEquals(1, (result as CinemaxResponse.Success).value.results?.size) // ✅ Check size
    }

    @Test
    fun `getByMediaType should return failure when API call fails`() = runTest {
        // Arrange
        val errorJson =
            "{\"status_message\": \"Bad Request\"}".toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<TvShowResponse>(400, errorJson)
        Mockito.`when`(tvShowApiService.getPopularTv()).thenReturn(errorResponse)

        // Act
        val result = tvShowNetworkDataSource.getByMediaType(NetworkMediaType.TvShow.POPULAR, 1)

        // Assert
        assert(result is CinemaxResponse.Failure)
        assertEquals("Bad Request", (result as CinemaxResponse.Failure).error)
    }

    @Test
    fun `getTvShowMovie should return success when API call is successful`() = runTest {
        // Arrange
        val movieId = 1
        val mockTvShowDetailNetwork = TvShowDetailNetwork(
            id = movieId,
            adult = false,
            name = "Sample Movie",
            overview = "This is a sample overview",
        )

        val mockResponse = Response.success(mockTvShowDetailNetwork)

        Mockito.`when`(tvShowApiService.getDetailsById(movieId)).thenReturn(mockResponse)

        // Act
        val result = tvShowNetworkDataSource.getDetailTv(movieId)
        val actualData = (result as CinemaxResponse.Success).value

        assert(true)
        assertEquals(mockTvShowDetailNetwork, actualData)
    }
}