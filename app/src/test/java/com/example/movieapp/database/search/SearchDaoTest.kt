package com.example.movieapp.database.search

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.movie.MovieDao
import com.example.movieapp.core.database.dao.search.SearchDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.times
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class SearchDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var searchDao: SearchDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        searchDao = database.searchHistoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveSearchHistory() = runBlocking {
        val searchEntity =
            SearchEntity(
                id = 12444,
                mediaType = "Movie",
                title = "Harry Potter and the Deathly Hallows: Part 1",
                overview = "Harry, Ron and Hermione walk away from their last year at Hogwarts to find and destroy the remaining Horcruxes, putting an end to Voldemort's bid for immortality. But with Harry's beloved Dumbledore dead and Voldemort's unscrupulous Death Eaters on the loose, the world is more dangerous than ever.",
                popularity = 0.0,
                releaseDate = "",
                adult = false,
                genreEntities = emptyList(),
                originalTitle = "",
                originalLanguage = "",
                voteAverage = 0.0,
                voteCount = 0,
                posterPath = "",
                backdropPath = "",
                video = false,
                rating = 3.9,
                runtime = 146,
                timestamp = 1735113324891
            )
        searchDao.insertSearchHistory(searchEntity)

        val history = searchDao.getSearchHistory().first() // Collect first emission
        assertEquals(1, history.size)
        assertEquals("Harry Potter and the Deathly Hallows: Part 1", history[0].title)
    }

    @Test
    fun deleteSearchHistory() = runBlocking {
        val searchEntity =
            SearchEntity(
                id = 12444,
                mediaType = "Movie",
                title = "Harry Potter and the Deathly Hallows: Part 1",
                overview = "Harry, Ron and Hermione walk away from their last year at Hogwarts to find and destroy the remaining Horcruxes, putting an end to Voldemort's bid for immortality. But with Harry's beloved Dumbledore dead and Voldemort's unscrupulous Death Eaters on the loose, the world is more dangerous than ever.",
                popularity = 0.0,
                releaseDate = "",
                adult = false,
                genreEntities = emptyList(),
                originalTitle = "",
                originalLanguage = "",
                voteAverage = 0.0,
                voteCount = 0,
                posterPath = "",
                backdropPath = "",
                video = false,
                rating = 3.9,
                runtime = 146,
                timestamp = 1735113324891
            )
        searchDao.insertSearchHistory(searchEntity)
        searchDao.deleteSearchHistory(12444)

        val history = searchDao.getSearchHistory().first()
        assertTrue(history.isEmpty())
    }

    @Test
    fun isSearchExist() = runBlocking {
        val searchEntity =
            SearchEntity(
                id = 12444,
                mediaType = "Movie",
                title = "Harry Potter and the Deathly Hallows: Part 1",
                overview = "Harry, Ron and Hermione walk away from their last year at Hogwarts to find and destroy the remaining Horcruxes, putting an end to Voldemort's bid for immortality. But with Harry's beloved Dumbledore dead and Voldemort's unscrupulous Death Eaters on the loose, the world is more dangerous than ever.",
                popularity = 0.0,
                releaseDate = "",
                adult = false,
                genreEntities = emptyList(),
                originalTitle = "",
                originalLanguage = "",
                voteAverage = 0.0,
                voteCount = 0,
                posterPath = "",
                backdropPath = "",
                video = false,
                rating = 3.9,
                runtime = 146,
                timestamp = 1735113324891
            )
        searchDao.insertSearchHistory(searchEntity)

        val exists = searchDao.isSearchExist(12444)
        assertTrue(exists)

        val notExists = searchDao.isSearchExist(12345)
        assertFalse(notExists)
    }
}