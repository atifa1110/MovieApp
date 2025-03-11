package com.example.movieapp.database.movie

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.movie.MovieDetailsDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.database.resource.movieDetail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class MovieDetailsDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var movieDetailDao: MovieDetailsDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        movieDetailDao = database.movieDetailsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndValidateIsNotEmpty() = runBlocking {
        movieDetailDao.insert(movieDetail)

        val result = movieDetailDao.getById(845781).first()

        assertEquals(true,result?.title?.isNotEmpty())
    }

    @Test
    fun insertAndRetrieveData() = runBlocking {
        movieDetailDao.insert(movieDetail)

        val result = movieDetailDao.getById(845781).first()

        assertEquals("Red One",result?.title)
        assertEquals("Released",result?.status)
    }

    @Test
    fun deleteDetailsById() = runBlocking {
        movieDetailDao.insert(movieDetail)

        movieDetailDao.deleteById(845781)

        val result = movieDetailDao.getById(845781).first()

        assertNull(result)
    }
}