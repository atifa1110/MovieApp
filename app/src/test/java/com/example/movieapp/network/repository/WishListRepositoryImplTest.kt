package com.example.movieapp.network.repository

import app.cash.turbine.test
import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.database.mapper.asWishlistModel
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.network.repository.WishListRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WishListRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule() // For testing coroutines

    @Mock
    private lateinit var databaseSource: WishlistDatabaseSource

    private lateinit var repository: WishListRepositoryImpl

    @Before
    fun setup() {
        repository = WishListRepositoryImpl(databaseSource)
    }

    @Test
    fun `getWishlist success`() = runTest {
        // Mock the database response
        val wishlistEntities = listOf(
            WishlistEntity(
                id = 0, networkId = 0, mediaType = MediaType.Wishlist.Movie, title = "Title",
                genreEntities = emptyList(), rating = 0.0, posterPath = "", isWishListed = true
            ),
            WishlistEntity(
                id = 1, networkId = 1, mediaType = MediaType.Wishlist.TvShow, title = "Title",
                genreEntities = emptyList(), rating = 0.0, posterPath = "", isWishListed = true
            )
        )

        val wishlistModels = wishlistEntities.map { it.asWishlistModel() }
        whenever(databaseSource.getWishlist()).thenReturn(flowOf(wishlistEntities))

        repository.getWishlist().test {
            assertEquals(CinemaxResponse.Loading, awaitItem())  // First emission should be Loading
            assertEquals(CinemaxResponse.Success(wishlistModels), awaitItem()) // Then Success with data
            awaitComplete() // Ensure no more emissions
        }
    }

    @Test
    fun `getWishlist() should emit Loading, then Failure when exception occurs`() = runTest {
        val errorMessage = "Database error"

        // Mock an exception when fetching wishlist
        whenever(databaseSource.getWishlist()).thenReturn(flow { throw RuntimeException(errorMessage) })

        // Collect flow emissions
        repository.getWishlist().test {
            assertEquals(CinemaxResponse.Loading, awaitItem())  // First emission should be Loading
            val failure = awaitItem()
            assertTrue(failure is CinemaxResponse.Failure && failure.error == errorMessage) // Then Failure with error message
            awaitComplete()
        }
    }

    @Test
    fun `addMovieToWishlist() should call databaseSource`() = runTest {
        val movie = MovieDetailModel(
            id = 1,adult =false, backdropPath = "", budget = 0, genres = emptyList(),
            homepage = "", imdbId = "", originalLanguage = "", originalTitle = "Title",
            overview = "", popularity = 0.0, posterPath = "", releaseDate = "", revenue = 0,
            runtime = 0, status = "", tagline = "", title = "Title", video =  false, voteAverage = 0.0,
            voteCount = 0, rating = 0.0, credits = CreditsModel(emptyList(), emptyList()),
            images = ImagesModel(emptyList(), emptyList()), videos =  VideosModel(emptyList()),
            isWishListed = true
        )

        repository.addMovieToWishlist(movie)

        verify(databaseSource).addMovieToWishlist(movie)
        verifyNoMoreInteractions(databaseSource)
    }

    @Test
    fun `addTvShowToWishlist() should call databaseSource`() = runTest {
        val tvShow = TvShowDetailModel(
            id = 1, name = "Title", adult = false, backdropPath = "", episodeRunTime = emptyList(),
            firstAirDate = "", genres =  emptyList(), seasons = emptyList(), homepage = "",
            inProduction = false, languages = emptyList(), lastAirDate = "", numberOfEpisodes = 0,
            numberOfSeasons = 0, originCountry = emptyList(), originalLanguage = "", originalName = "",
            overview = "", popularity = 0.0, posterPath = "", status = "", tagline = "",
            type = "", voteAverage = 0.0, voteCount = 0, credits = CreditsModel(emptyList(), emptyList()),
            rating = 0.0, isWishListed = false
        )

        repository.addTvShowToWishlist(tvShow)

        verify(databaseSource).addTvShowToWishlist(tvShow)
        verifyNoMoreInteractions(databaseSource)
    }

    @Test
    fun `removeMovieFromWishlist() should call databaseSource`() = runTest {
        val movie = MovieDetailModel(
            id = 1,adult =false, backdropPath = "", budget = 0, genres = emptyList(),
            homepage = "", imdbId = "", originalLanguage = "", originalTitle = "Title",
            overview = "", popularity = 0.0, posterPath = "", releaseDate = "", revenue = 0,
            runtime = 0, status = "", tagline = "", title = "Title", video =  false, voteAverage = 0.0,
            voteCount = 0, rating = 0.0, credits = CreditsModel(emptyList(), emptyList()),
            images = ImagesModel(emptyList(), emptyList()), videos =  VideosModel(emptyList()),
            isWishListed = true
        )

        repository.addMovieToWishlist(movie)
        verify(databaseSource).addMovieToWishlist(movie)

        val movieId = 1

        repository.removeMovieFromWishlist(movieId)

        verify(databaseSource).removeMovieFromWishlist(movieId)
        verifyNoMoreInteractions(databaseSource)
    }

    @Test
    fun `removeTvShowFromWishlist() should call databaseSource`() = runTest {
        val tvShow = TvShowDetailModel(
            id = 1, name = "Title", adult = false, backdropPath = "", episodeRunTime = emptyList(),
            firstAirDate = "", genres =  emptyList(), seasons = emptyList(), homepage = "",
            inProduction = false, languages = emptyList(), lastAirDate = "", numberOfEpisodes = 0,
            numberOfSeasons = 0, originCountry = emptyList(), originalLanguage = "", originalName = "",
            overview = "", popularity = 0.0, posterPath = "", status = "", tagline = "",
            type = "", voteAverage = 0.0, voteCount = 0, credits = CreditsModel(emptyList(), emptyList()),
            rating = 0.0, isWishListed = false
        )

        repository.addTvShowToWishlist(tvShow)
        verify(databaseSource).addTvShowToWishlist(tvShow)
        val tvShowId = 1

        repository.removeTvShowFromWishlist(tvShowId)

        verify(databaseSource).removeTvShowFromWishlist(tvShowId)
        verifyNoMoreInteractions(databaseSource)
    }
}