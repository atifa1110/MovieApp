package com.example.movieapp.database.source

import androidx.paging.PagingSource
import com.example.movieapp.core.database.dao.genre.GenreDao
import com.example.movieapp.core.database.dao.movie.MovieDao
import com.example.movieapp.core.database.dao.movie.MovieRemoteKeyDao
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import org.mockito.kotlin.*


@RunWith(RobolectricTestRunner::class)
class MovieDatabaseSourceTest {

    // Mock dependencies
    private lateinit var movieDao: MovieDao
    private lateinit var movieRemoteKeyDao: MovieRemoteKeyDao
    private lateinit var genreDao: GenreDao
    private lateinit var transactionProvider: FakeTransactionProvider

    // Class under test
    private lateinit var movieDatabaseSource: MovieDatabaseSource

    @Before
    fun setUp() {
        // Initialize mocks
        movieDao = mock(MovieDao::class.java)
        movieRemoteKeyDao = mock(MovieRemoteKeyDao::class.java)
        genreDao = mock(GenreDao::class.java)
        transactionProvider = mock(FakeTransactionProvider::class.java)

        // Initialize the class under test
        movieDatabaseSource = MovieDatabaseSource(
            movieDao,
            movieRemoteKeyDao,
            genreDao,
            transactionProvider
        )
    }

    @After
    fun tearDown() {
        // Optional: Reset mocks after each test
        reset(movieDao, movieRemoteKeyDao, genreDao, transactionProvider)
    }

    @Test
    fun `getGenreMovie should emit genre list from genreDao`() = runTest {
        // Arrange
        val mockGenreList = listOf(GenreEntity(1, "Action"), GenreEntity(2, "Drama"))
        whenever(genreDao.getGenres()).thenReturn(flowOf(mockGenreList))

        // Act
        val result = movieDatabaseSource.getGenreMovie().first()

        // Assert
        assertEquals(mockGenreList, result)
        verify(genreDao).getGenres()
    }

    @Test
    fun `getMoviesWithGenres should combine movies and genres correctly`() = runTest {
        // Arrange
        val mockMovieList = listOf(
            MovieEntity(
                id = 1, mediaType = MediaType.Movie.Popular, networkId = 1, title = "Harry Potter and the Philosopher's Stone",
                overview = "", popularity = 234.43, releaseDate = "2001-11-16", adult = false, genreIds = listOf(1,2),
                originalTitle = "Harry Potter and the Philosopher's Stone", originalLanguage = "en",
                voteAverage = 7.9, voteCount = 27593, posterPath = "", backdropPath = "", video = false,
                rating = 0.0, runtime = 0
            ),
            MovieEntity(
                id = 2,mediaType = MediaType.Movie.Popular, networkId = 2, title = "Sonic the Hedgehog 3",
                overview = "", popularity = 4014.717, releaseDate = "2024-12-19", adult = false, genreIds = listOf(2),
                originalTitle = "Sonic the Hedgehog 3", originalLanguage = "en", voteAverage = 7.774,
                voteCount = 1626, posterPath = "", backdropPath = "", video = false, rating = 0.0, runtime = 0
            )
        )

        val mockGenreMap = mapOf(
            1 to GenreEntity(1, "Action"),
            2 to GenreEntity(2, "Drama")
        )

        val expectedMoviesWithGenres = listOf(
            MovieWithGenres(
                movie = mockMovieList[0],
                genreNames = listOf("Action", "Drama")
            ),
            MovieWithGenres(
                movie = mockMovieList[1],
                genreNames = listOf("Drama")
            )
        )

        // Mocking DAO responses
        whenever(movieDao.getByMediaType(any(), any())).thenReturn(flowOf(mockMovieList))

        whenever(genreDao.getGenresByIds(any())).thenAnswer { invocation ->
            val genreIds = invocation.arguments[0] as List<Int>
            flowOf(genreIds.mapNotNull { mockGenreMap[it] }) // Ensures correct genre mapping
        }

        // Act
        val result = movieDatabaseSource.getMoviesWithGenres(MediaType.Movie.Popular, 10).first()

        // Assert
        assertEquals(expectedMoviesWithGenres, result)
        verify(movieDao).getByMediaType(MediaType.Movie.Popular, 10)
        verify(genreDao).getGenresByIds(listOf(1, 2))
        verify(genreDao).getGenresByIds(listOf(2))
    }

    @Test
    fun `getByMediaType should return movies from movieDao`() = runTest {
        // Given
        val mockMovieList = listOf(
            MovieEntity(
                id = 1, mediaType = MediaType.Movie.Popular, networkId = 1, title = "Harry Potter and the Philosopher's Stone",
                overview = "", popularity = 234.43, releaseDate = "2001-11-16", adult = false, genreIds = listOf(1,2),
                originalTitle = "Harry Potter and the Philosopher's Stone", originalLanguage = "en",
                voteAverage = 7.9, voteCount = 27593, posterPath = "", backdropPath = "", video = false,
                rating = 0.0, runtime = 0
            ),
            MovieEntity(
                id = 2,mediaType = MediaType.Movie.Popular, networkId = 2, title = "Sonic the Hedgehog 3",
                overview = "", popularity = 4014.717, releaseDate = "2024-12-19", adult = false, genreIds = listOf(2),
                originalTitle = "Sonic the Hedgehog 3", originalLanguage = "en", voteAverage = 7.774,
                voteCount = 1626, posterPath = "", backdropPath = "", video = false, rating = 0.0, runtime = 0
            )
        )

        whenever(movieDao.getByMediaType(any(), any())).thenReturn(flowOf(mockMovieList))

        // When
        val result = movieDatabaseSource.getByMediaType(MediaType.Movie.Popular, 10).first()

        // Then
        assertEquals(mockMovieList, result)
        assertEquals(2, mockMovieList.size)
        assertEquals(2, result.size)
    }

    @Test
    fun `getPagingByMediaType should return PagingSource from movieDao`() {
        // Given
        val pagingSource = mock(PagingSource::class.java) as PagingSource<Int, MovieEntity>
        whenever(movieDao.getPagingByMediaType(any())).thenReturn(pagingSource)

        // When
        val result = movieDatabaseSource.getPagingByMediaType(MediaType.Movie.Popular)

        // Then
        assertEquals(pagingSource, result)
    }

    @Test
    fun `insertGenre should execute transaction and insert genres`() = runTest {
        // Arrange
        val mockGenres = listOf(GenreEntity(1, "Action"), GenreEntity(2, "Drama"))
        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        movieDatabaseSource.insertGenre(mockGenres)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert database interactions
        verify(genreDao).deleteAll()
        verify(genreDao).insertAll(mockGenres)
    }

    @Test
    fun `deleteByMediaTypeAndInsertAll should call delete and insert methods`() = runTest {
        // Arrange
        val mediaType = MediaType.Movie.Popular
        val newMovies = listOf(
            MovieEntity(
                id = 1, mediaType = MediaType.Movie.Popular, networkId = 1, title = "Harry Potter and the Philosopher's Stone",
                overview = "", popularity = 234.43, releaseDate = "2001-11-16", adult = false, genreIds = listOf(1,2),
                originalTitle = "Harry Potter and the Philosopher's Stone", originalLanguage = "en",
                voteAverage = 7.9, voteCount = 27593, posterPath = "", backdropPath = "", video = false,
                rating = 0.0, runtime = 0
            ),
            MovieEntity(
                id = 2,mediaType = MediaType.Movie.Popular, networkId = 2, title = "Sonic the Hedgehog 3",
                overview = "", popularity = 4014.717, releaseDate = "2024-12-19", adult = false, genreIds = listOf(2),
                originalTitle = "Sonic the Hedgehog 3", originalLanguage = "en", voteAverage = 7.774,
                voteCount = 1626, posterPath = "", backdropPath = "", video = false, rating = 0.0, runtime = 0
            )
        )

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        movieDatabaseSource.deleteByMediaTypeAndInsertAll(mediaType, newMovies)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        verify(movieDao).deleteByMediaType(mediaType) // Ensures deletion happened
        verify(movieDao).insertAll(newMovies)         // Ensures insertion happened
    }

    @Test
    fun `handlePaging should delete and insert when shouldDeleteMoviesAndRemoteKeys is true`() = runTest {
        // Arrange
        val mediaType = MediaType.Movie.Popular
        val mockMovieList = listOf(
            MovieEntity(
                id = 1, mediaType = MediaType.Movie.Popular, networkId = 1, title = "Harry Potter and the Philosopher's Stone",
                overview = "", popularity = 234.43, releaseDate = "2001-11-16", adult = false, genreIds = listOf(1,2),
                originalTitle = "Harry Potter and the Philosopher's Stone", originalLanguage = "en",
                voteAverage = 7.9, voteCount = 27593, posterPath = "", backdropPath = "", video = false,
                rating = 0.0, runtime = 0
            ),
            MovieEntity(
                id = 2,mediaType = MediaType.Movie.Popular, networkId = 2, title = "Sonic the Hedgehog 3",
                overview = "", popularity = 4014.717, releaseDate = "2024-12-19", adult = false, genreIds = listOf(2),
                originalTitle = "Sonic the Hedgehog 3", originalLanguage = "en", voteAverage = 7.774,
                voteCount = 1626, posterPath = "", backdropPath = "", video = false, rating = 0.0, runtime = 0
            )
        )
        val remoteKeys = listOf(MovieRemoteKeyEntity(id = 1, mediaType = mediaType,1,2))

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        movieDatabaseSource.handlePaging(mediaType, mockMovieList, remoteKeys, shouldDeleteMoviesAndRemoteKeys = true)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(movieDao, Mockito.times(1)).deleteByMediaType(mediaType)
        verify(movieRemoteKeyDao, Mockito.times(1)).deleteByMediaType(mediaType)
        verify(movieRemoteKeyDao, Mockito.times(1)).insertAll(remoteKeys)
        verify(movieDao, Mockito.times(1)).insertAll(mockMovieList)
    }

    @Test
    fun `handlePaging should only insert when shouldDeleteMoviesAndRemoteKeys is false`() = runTest {
        // Arrange
        val mediaType = MediaType.Movie.Popular
        val mockMovieList = listOf(
            MovieEntity(
                id = 1, mediaType = MediaType.Movie.Popular, networkId = 1, title = "Harry Potter and the Philosopher's Stone",
                overview = "", popularity = 234.43, releaseDate = "2001-11-16", adult = false, genreIds = listOf(1,2),
                originalTitle = "Harry Potter and the Philosopher's Stone", originalLanguage = "en",
                voteAverage = 7.9, voteCount = 27593, posterPath = "", backdropPath = "", video = false,
                rating = 0.0, runtime = 0
            ),
            MovieEntity(
                id = 2,mediaType = MediaType.Movie.Popular, networkId = 2, title = "Sonic the Hedgehog 3",
                overview = "", popularity = 4014.717, releaseDate = "2024-12-19", adult = false, genreIds = listOf(2),
                originalTitle = "Sonic the Hedgehog 3", originalLanguage = "en", voteAverage = 7.774,
                voteCount = 1626, posterPath = "", backdropPath = "", video = false, rating = 0.0, runtime = 0
            )
        )
        val remoteKeys = listOf(MovieRemoteKeyEntity(id = 1, mediaType = mediaType,1,2))

        val transactionCaptor = argumentCaptor<suspend () -> Unit>()

        // Act
        movieDatabaseSource.handlePaging(mediaType, mockMovieList, remoteKeys, shouldDeleteMoviesAndRemoteKeys = false)

        // Verify transaction block is captured
        verify(transactionProvider).runWithTransaction(transactionCaptor.capture())

        // Execute the captured transaction manually
        transactionCaptor.firstValue.invoke()

        // Assert
        verify(movieDao, Mockito.never()).deleteByMediaType(mediaType)
        verify(movieRemoteKeyDao, Mockito.never()).deleteByMediaType(mediaType)
        verify(movieRemoteKeyDao, Mockito.times(1)).insertAll(remoteKeys)
        verify(movieDao, Mockito.times(1)).insertAll(mockMovieList)
    }
}