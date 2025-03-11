package com.example.movieapp.network.repository

import app.cash.turbine.test
import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.database.source.MovieDetailsDatabaseSource
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.repository.MovieDetailRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.database.resource.movieDetail
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class MovieDetailRepositoryImplTest {

    // Mock dependencies
    private val databaseDataSource: MovieDetailsDatabaseSource = mock()
    private val networkDataSource: MovieNetworkDataSource = mock()
    private val wishlistDatabaseSource: WishlistDatabaseSource = mock()

    // Repository under test
    private lateinit var repository: MovieDetailRepositoryImpl

    @Before
    fun setUp() {
        repository = MovieDetailRepositoryImpl(
            databaseDataSource,
            networkDataSource,
            wishlistDatabaseSource
        )
    }

    @Test
    fun `getDetailMovie should emit loading, fetch from network, save to DB, and return success`() = runTest {
        val movieId = 1
        val mockMovieEntity = MovieDetailsEntity(
            id = movieId, adult = false, backdropPath = "", budget = 0, genreEntities = listOf(),
            homepage = "", imdbId = "tt0241527", originalLanguage = "", originalTitle =   "Test Movie",
            overview = "", popularity = 0.0, posterPath = "", releaseDate = "", revenue = 0,
            runtime = 0, status = "", tagline = "", title =   "Test Movie",
            video = false, voteAverage = 0.0, voteCount = 0, rating = 0.0,
            credits = CreditsEntity(listOf(), listOf()), images = ImagesEntity(listOf(), listOf()),
            videos = VideosEntity(listOf())
        )

        val mockMovieDetail = MovieDetailModel(
            id = movieId, adult = false, backdropPath = "", budget = 0, genres = listOf(),
            homepage = "", imdbId = "tt0241527", originalLanguage = "", originalTitle = "Test Movie",
            overview = "", popularity = 0.0, posterPath = "", releaseDate = "", revenue = 0,
            runtime = 0, status = "", tagline = "", title =   "Test Movie", video = false,
            voteAverage = 0.0, voteCount = 0, rating = 0.0,
            credits = CreditsModel(listOf(), listOf()),
            images = ImagesModel(listOf(), listOf()),
            videos = VideosModel(listOf()), isWishListed = false
        )

        val mockApiResponse = MovieDetailNetwork(
            id = movieId,
            adult = false,
            title = "Test Movie",
            overview = "This is a sample overview",
        )

        // Mock database and network calls
        whenever(databaseDataSource.getById(movieId)).thenReturn(flowOf(mockMovieEntity))
        whenever(networkDataSource.getDetailMovie(movieId)).thenReturn(CinemaxResponse.Success(mockApiResponse))
        whenever(wishlistDatabaseSource.isMovieWishListed(movieId)).thenReturn(false)

        val results = repository.getDetailMovie(movieId).toList()
        // Assert
        assertEquals(3, results.size) // Loading, Loading, Success

        assertTrue(results[0] is CinemaxResponse.Loading)
        assertTrue(results[1] is CinemaxResponse.Loading)
        assertTrue(results[2] is CinemaxResponse.Success)

        val successResult = results[2] as CinemaxResponse.Success
        assertEquals(mockMovieDetail, successResult.value) // Check if the final data is correct

        // Verify that the fetched movie details were saved to the database
        verify(databaseDataSource).deleteAndInsert(any())
    }

    @Test
    fun `getDetailMovie should emit failure when network fetch fails`() = runTest {
        val movieId = 1
        val errorMessage = "Network Error"

        // Mock DB returning null and network throwing an error
        whenever(databaseDataSource.getById(movieId)).thenReturn(flowOf(null))
        whenever(networkDataSource.getDetailMovie(movieId)).thenReturn(CinemaxResponse.Failure("Network Error"))

        val results = repository.getDetailMovie(movieId).toList()

        Assert.assertTrue(results[2] is CinemaxResponse.Failure)

        val failureResult = results[2] as CinemaxResponse.Failure
        assertEquals(errorMessage, failureResult.error)
    }
}
