package com.example.movieapp.database.movie

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.movie.MovieRemoteKeyDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MovieRemoteKeyDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var movieRemoteKeyDao: MovieRemoteKeyDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        movieRemoteKeyDao = database.movieRemoteKeyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieve() = runBlocking {
        val remoteKey = MovieRemoteKeyEntity(id = 1, mediaType = MediaType.Movie.Popular, prevPage = 1,nextPage = 2)
        movieRemoteKeyDao.insertAll(listOf(remoteKey))

        val result = movieRemoteKeyDao.getByIdAndMediaType(1, MediaType.Movie.Popular)
        assertEquals(remoteKey, result)
    }

    @Test
    fun testDeleteByMediaType() = runBlocking {
        val remoteKeys = listOf(
            MovieRemoteKeyEntity(id = 1, mediaType = MediaType.Movie.Popular, prevPage = 1,nextPage = 2),
            MovieRemoteKeyEntity(id = 2, mediaType = MediaType.Movie.Trending, prevPage = 1,nextPage = 2)
        )
        movieRemoteKeyDao.insertAll(remoteKeys)
        movieRemoteKeyDao.deleteByMediaType(MediaType.Movie.Popular)

        val result = movieRemoteKeyDao.getByIdAndMediaType(1, MediaType.Movie.Popular)
        assertNull(result)
    }
}