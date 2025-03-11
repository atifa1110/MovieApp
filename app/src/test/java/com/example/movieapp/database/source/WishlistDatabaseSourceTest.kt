package com.example.movieapp.database.source

import com.example.movieapp.core.database.dao.wishlist.WishlistDao
import com.example.movieapp.core.database.mapper.asWishlistEntity
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.VideosModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class WishlistDatabaseSourceTest {

    private lateinit var wishlistDao: WishlistDao
    private lateinit var wishlistDatabaseSource: WishlistDatabaseSource

    @Before
    fun setUp() {
        // Mock DAO
        wishlistDao = mock(WishlistDao::class.java)

        // Initialize class under test
        wishlistDatabaseSource = WishlistDatabaseSource(wishlistDao)
    }

    @Test
    fun `getWishlist should return correct wishlist`() = runTest {
        // Arrange
        val wishlistItems = listOf(mock(WishlistEntity::class.java))
        whenever(wishlistDao.getByMediaType()).thenReturn(flowOf(wishlistItems))

        // Act
        val result = wishlistDatabaseSource.getWishlist().first()

        // Assert
        assertEquals(wishlistItems, result)
    }

    @Test
    fun `addMovieToWishlist should call insert with correct entity`() = runTest {
        // Arrange
        val movie = MovieDetailModel(
            id = 0,
            adult = false,
            backdropPath = "",
            budget = 0,
            genres = listOf(),
            homepage = "",
            imdbId = "",
            originalLanguage = "",
            originalTitle = "",
            overview = "",
            popularity = 0.0,
            posterPath = "",
            releaseDate = "",
            revenue = 0,
            runtime = 0,
            status = "",
            tagline = "",
            title = "",
            video = false,
            voteAverage = 0.0,
            voteCount = 0,
            rating = 0.0,
            credits = CreditsModel(listOf(), listOf()),
            images = ImagesModel(listOf(), listOf()),
            videos = VideosModel(listOf()),
            isWishListed = false
        )

        val wishlistEntity = MediaType.Wishlist.Movie.asWishlistEntity(movie)
        whenever(wishlistDao.insert(any())).thenReturn(Unit)

        // Act
        wishlistDatabaseSource.addMovieToWishlist(movie)

        // Assert
        verify(wishlistDao).insert(wishlistEntity)
    }

    @Test
    fun `addTvShowToWishlist should call insert with correct entity`() = runTest {
        // Arrange
        val tvShow = TvShowDetailModel(
            id = 1,
            name = "Squid Game",
            adult = false,
            backdropPath = "",
            episodeRunTime = listOf(),
            firstAirDate = "",
            genres = listOf(),
            seasons = listOf(),
            homepage = "",
            inProduction = false,
            languages = listOf(),
            lastAirDate = "",
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            originCountry =  listOf(),
            originalLanguage = "ko",
            originalName = "오징어 게임",
            overview = "",
            popularity = 0.0,
            posterPath = "",
            status = "Returning Series",
            tagline = "Prepare for the final game.",
            type = "",
            voteAverage = 0.0,
            voteCount = 0,
            credits = CreditsModel(listOf(), listOf()),
            rating = 0.0,
            isWishListed = false
        )
        val wishlistEntity = MediaType.Wishlist.TvShow.asWishlistEntity(tvShow)
        whenever(wishlistDao.insert(any())).thenReturn(Unit)

        // Act
        wishlistDatabaseSource.addTvShowToWishlist(tvShow)

        // Assert
        verify(wishlistDao).insert(wishlistEntity)
    }

    @Test
    fun `removeMovieFromWishlist should call deleteByMediaTypeAndNetworkId`() = runTest {
        // Act
        wishlistDatabaseSource.removeMovieFromWishlist(1)

        // Assert
        verify(wishlistDao).deleteByMediaTypeAndNetworkId(MediaType.Wishlist.Movie, 1)
    }

    @Test
    fun `removeTvShowFromWishlist should call deleteByMediaTypeAndNetworkId`() = runTest {
        // Act
        wishlistDatabaseSource.removeTvShowFromWishlist(2)

        // Assert
        verify(wishlistDao).deleteByMediaTypeAndNetworkId(MediaType.Wishlist.TvShow, 2)
    }

    @Test
    fun `isMovieWishListed should return correct boolean`() = runTest {
        // Arrange
        whenever(wishlistDao.isWishListed(MediaType.Wishlist.Movie, 1)).thenReturn(true)

        // Act
        val result = wishlistDatabaseSource.isMovieWishListed(1)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isTvShowWishListed should return correct boolean`() = runTest {
        // Arrange
        whenever(wishlistDao.isWishListed(MediaType.Wishlist.TvShow, 2)).thenReturn(true)

        // Act
        val result = wishlistDatabaseSource.isTvShowWishListed(2)

        // Assert
        assertTrue(result)
    }
}
