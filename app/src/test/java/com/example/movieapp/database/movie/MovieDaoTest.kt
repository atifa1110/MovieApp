package com.example.movieapp.database.movie

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.movie.MovieDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.database.resource.movies
import com.example.movieapp.database.resource.trendingMovies
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MovieDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        movieDao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndValidateIsNotEmpty() = runBlocking {
        movieDao.insertAll(movies)

        val result = movieDao.getByMediaType(MediaType.Movie.Popular, 10).first()

        assertEquals(true,result.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun insertAndRetrieveMovies() = runBlocking {
        movieDao.insertAll(movies)

        val result = movieDao.getByMediaType(MediaType.Movie.Popular, 10).first()

        assertEquals(result[0].title,"Red One")
        assertEquals(result.size,1)
    }


    @Test
    fun deleteMoviesByMediaType() = runBlocking {
        movieDao.insertAll(movies)
        movieDao.insertAll(trendingMovies)

        // Act
        movieDao.deleteByMediaType(MediaType.Movie.Popular)

        val popularData = movieDao.getByMediaType(MediaType.Movie.Popular, 10).first()
        val trendingData = movieDao.getByMediaType(MediaType.Movie.Trending, 10).first()

        // Assert
        assertEquals(0, popularData.size)
        assertEquals(popularData.isNotEmpty(), false)
        assertEquals(trendingData.isNotEmpty(), true)
        assert(popularData.isEmpty())
        assert(trendingData.isNotEmpty())
    }

    @Test
    fun insertAndRetrievePagingSource() = runBlocking {
        // Arrange
        movieDao.insertAll(movies)

        val pagingSource = movieDao.getPagingByMediaType(MediaType.Movie.Popular)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assert(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals("Red One", page.data[0].title)

    }
}