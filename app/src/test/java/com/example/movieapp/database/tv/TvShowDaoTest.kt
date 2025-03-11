package com.example.movieapp.database.tv

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.tv.TvShowDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.database.resource.trendingTvShows
import com.example.movieapp.database.resource.tvShows
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TvShowDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var tvShowDao: TvShowDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        tvShowDao = database.tvShowDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndValidateIsNotEmpty() = runBlocking {
        tvShowDao.insertAll(tvShows)

        val result = tvShowDao.getByMediaType(MediaType.TvShow.Popular, 10).first()

        assertEquals(true,result.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun insertAndRetrieveMovies() = runBlocking {
        tvShowDao.insertAll(tvShows)

        val result = tvShowDao.getByMediaType(MediaType.TvShow.Popular, 10).first()

        assertEquals(result[0].name,"Squid Game")
        assertEquals(result.size,1)
    }


    @Test
    fun deleteMoviesByMediaType() = runBlocking {
        tvShowDao.insertAll(tvShows)
        tvShowDao.insertAll(trendingTvShows)

        // Act
        tvShowDao.deleteByMediaType(MediaType.TvShow.Popular)

        val popularData = tvShowDao.getByMediaType(MediaType.TvShow.Popular, 10).first()
        val trendingData = tvShowDao.getByMediaType(MediaType.TvShow.Trending, 10).first()

        // Assert
        Assert.assertEquals(0, popularData.size)
        Assert.assertEquals(popularData.isNotEmpty(), false)
        Assert.assertEquals(trendingData.isNotEmpty(), true)
        assert(popularData.isEmpty())
        assert(trendingData.isNotEmpty())
    }

    @Test
    fun insertAndRetrievePagingSource() = runBlocking {
        // Arrange
        tvShowDao.insertAll(tvShows)

        val pagingSource = tvShowDao.getPagingByMediaType(MediaType.TvShow.Popular)
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
        assertEquals("Squid Game", page.data[0].name)

    }
}