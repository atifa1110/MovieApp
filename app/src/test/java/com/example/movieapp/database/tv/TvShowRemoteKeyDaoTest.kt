package com.example.movieapp.database.tv

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.movie.MovieRemoteKeyDao
import com.example.movieapp.core.database.dao.tv.TvShowRemoteKeyDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class TvShowRemoteKeyDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var tvShowRemoteKeyDao: TvShowRemoteKeyDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        tvShowRemoteKeyDao = database.tvShowRemoteKeyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieve() = runBlocking {
        val remoteKey = TvShowRemoteKeyEntity(1,MediaType.TvShow.Popular,1,2)
        tvShowRemoteKeyDao.insertAll(listOf(remoteKey))

        val result = tvShowRemoteKeyDao.getByIdAndMediaType(1, MediaType.TvShow.Popular)
        assertEquals(remoteKey, result)
    }

    @Test
    fun testDeleteByMediaType() = runBlocking {
        val remoteKeys = listOf(
            TvShowRemoteKeyEntity(1,MediaType.TvShow.Popular,1,2),
            TvShowRemoteKeyEntity(2,MediaType.TvShow.Trending,1,2)
        )
        tvShowRemoteKeyDao.insertAll(remoteKeys)
        tvShowRemoteKeyDao.deleteByMediaType(MediaType.TvShow.Popular)

        val result = tvShowRemoteKeyDao.getByIdAndMediaType(1, MediaType.TvShow.Popular)
        Assert.assertNull(result)
    }
}