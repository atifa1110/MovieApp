package com.example.movieapp.database.tv

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.tv.TvShowDetailsDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.database.resource.tvShowDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class TvShowDetailsDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var tvShowDetailsDao: TvShowDetailsDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        tvShowDetailsDao = database.tvShowDetailsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndValidateIsNotEmpty() = runBlocking {
        tvShowDetailsDao.insert(tvShowDetails)

        val result = tvShowDetailsDao.getById(93405).first()

        assertEquals(true,result?.name?.isNotEmpty())
    }

    @Test
    fun insertAndRetrieveData() = runBlocking {
        tvShowDetailsDao.insert(tvShowDetails)

        val result = tvShowDetailsDao.getById(93405).first()

        assertEquals("Squid Game",result?.name)
        assertEquals("Returning Series",result?.status)
    }

    @Test
    fun deleteDetailsById() = runBlocking {
        tvShowDetailsDao.insert(tvShowDetails)

        tvShowDetailsDao.deleteById(93405)

        val result = tvShowDetailsDao.getById(93405).first()

        assertNull(result)
    }
}