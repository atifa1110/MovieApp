package com.example.movieapp.database.source

import com.example.movieapp.core.database.dao.movie.MovieDetailsDao
import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.database.source.MovieDetailsDatabaseSource
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class MovieDetailsDatabaseSourceTest {

    private lateinit var movieDetailsDao: MovieDetailsDao
    private lateinit var transactionProvider: DatabaseTransactionProvider
    private lateinit var movieDetailsDatabaseSource: MovieDetailsDatabaseSource

    @Before
    fun setUp() {
        // Mock dependencies
        movieDetailsDao = mock(MovieDetailsDao::class.java)
        transactionProvider = mock(DatabaseTransactionProvider::class.java)

        // Initialize class under test
        movieDetailsDatabaseSource = MovieDetailsDatabaseSource(
            movieDetailsDao = movieDetailsDao,
            transactionProvider = transactionProvider
        )
    }

    @Test
    fun `getById should return correct movie details`() = runTest {
        // Arrange
        val movieDetails = MovieDetailsEntity(
            id = 1,
            adult = false,
            backdropPath = "",
            budget = 0,
            genreEntities = listOf(),
            homepage = "",
            imdbId = "tt0241527",
            originalLanguage = "",
            originalTitle =  "Harry Potter and the Philosopher's Stone",
            overview = "",
            popularity = 0.0,
            posterPath = "",
            releaseDate = "",
            revenue = 0,
            runtime = 0,
            status = "",
            tagline = "",
            title =  "Harry Potter and the Philosopher's Stone",
            video = false,
            voteAverage = 0.0,
            voteCount = 0,
            rating = 0.0,
            credits = CreditsEntity(listOf(), listOf()),
            images = ImagesEntity(listOf(), listOf()),
            videos = VideosEntity(listOf())
        )
        whenever(movieDetailsDao.getById(1)).thenReturn(flowOf(movieDetails))

        // Act
        val result = movieDetailsDatabaseSource.getById(1).first()

        // Assert
        assertEquals(movieDetails, result) // Ensure the returned result matches
    }

    @Test
    fun `deleteAndInsert should delete and insert correctly`() = runTest {
        // Arrange
        val movieDetails = MovieDetailsEntity(
            id = 1,
            adult = false,
            backdropPath = "",
            budget = 0,
            genreEntities = listOf(),
            homepage = "",
            imdbId = "tt0241527",
            originalLanguage = "",
            originalTitle =  "Harry Potter and the Philosopher's Stone",
            overview = "",
            popularity = 0.0,
            posterPath = "",
            releaseDate = "",
            revenue = 0,
            runtime = 0,
            status = "",
            tagline = "",
            title =  "Harry Potter and the Philosopher's Stone",
            video = false,
            voteAverage = 0.0,
            voteCount = 0,
            rating = 0.0,
            credits = CreditsEntity(listOf(), listOf()),
            images = ImagesEntity(listOf(), listOf()),
            videos = VideosEntity(listOf())
        )

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        movieDetailsDatabaseSource.deleteAndInsert(movieDetails)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(movieDetailsDao).deleteById(movieDetails.id) // Ensure delete was called
        verify(movieDetailsDao).insert(movieDetails) // Ensure insert was called after delete
    }
}
