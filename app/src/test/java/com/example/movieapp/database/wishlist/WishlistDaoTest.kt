package com.example.movieapp.database.wishlist

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.database.dao.wishlist.WishlistDao
import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class WishlistDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var wishlistDao: WishlistDao

    @Before
    fun setup() {
        // Membuat database in-memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        wishlistDao = database.wishListDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertAndRetrieveWishlist() = runBlocking {
        val wishlistEntity = WishlistEntity(
            id = 1,
            networkId = 999,
            mediaType = MediaType.Wishlist.Movie,
            title = "Red One",
            genreEntities = null,
            rating = 0.0,
            posterPath = "",
            isWishListed = true
        )
        wishlistDao.insert(wishlistEntity)

        val wishlist = wishlistDao.getByMediaType().first() // Collect first emission
        assertEquals(1, wishlist.size)
        assertEquals("Red One", wishlist[0].title)
    }

    @Test
    fun deleteByMediaTypeAndNetworkId() = runBlocking {
        val wishlistEntity = WishlistEntity(
            id = 1,
            networkId = 999,
            mediaType = MediaType.Wishlist.Movie,
            title = "Red One",
            genreEntities = null,
            rating = 0.0,
            posterPath = "",
            isWishListed = true
        )
        wishlistDao.insert(wishlistEntity)
        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.Movie, 999)

        val wishlist = wishlistDao.getByMediaType().first()
        assertTrue(wishlist.isEmpty())
    }

    @Test
    fun isWishListed() = runBlocking {
        val wishlistEntity = WishlistEntity(
            id = 1,
            networkId = 999,
            mediaType = MediaType.Wishlist.Movie,
            title = "Red One",
            genreEntities = null,
            rating = 0.0,
            posterPath = "",
            isWishListed = true
        )
        wishlistDao.insert(wishlistEntity)

        val exists = wishlistDao.isWishListed(MediaType.Wishlist.Movie, 999)
        assertTrue(exists)

        wishlistDao.deleteByMediaTypeAndNetworkId(MediaType.Wishlist.Movie, 999)

        val notExists = wishlistDao.isWishListed(MediaType.Wishlist.Movie, 999)
        assertFalse(notExists)
    }

}