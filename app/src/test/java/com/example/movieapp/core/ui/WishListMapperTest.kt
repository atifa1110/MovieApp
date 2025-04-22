package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.model.WishList
import org.junit.Assert.assertEquals
import org.junit.Test

class WishListMapperTest {

    @Test
    fun `asWishlist correctly maps WishlistModel to WishList`() {
        // Given
        val wishlistModel = WishlistModel(
            id = 42,
            mediaType = MediaTypeModel.Wishlist.Movie,
            genre = emptyList(), // assuming .asGenres() handles empty list
            title = "The Wishful Movie",
            rating = 4.9,
            posterPath = "/wishful.jpg",
            isWishListed = true
        )

        // When
        val result: WishList = wishlistModel.asWishlist()

        // Then
        assertEquals(wishlistModel.id, result.id)
        assertEquals(wishlistModel.mediaType, result.mediaType)
        assertEquals(wishlistModel.genre?.size, result.genres?.size)
        assertEquals(wishlistModel.title, result.title)
        assertEquals(wishlistModel.rating, result.rating, 0.001)
        assertEquals(wishlistModel.posterPath, result.posterPath)
        assertEquals(wishlistModel.isWishListed, result.isWishListed)
    }
}
