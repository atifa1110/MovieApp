package com.example.movieapp.usecase.wishlist

import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.wishlist.usecase.DeleteMovieFromWishlistUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DeleteMovieFromWishlistUseCaseTest {

    private lateinit var repository: WishListRepository
    private lateinit var useCase: DeleteMovieFromWishlistUseCase
    private val movieId = 123

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteMovieFromWishlistUseCase(repository)
    }

    @Test
    fun `invoke should call removeMovieFromWishlist with correct id`() = runBlocking {
        coEvery { repository.removeMovieFromWishlist(movieId) } returns Unit

        useCase.invoke(movieId)

        coVerify(exactly = 1) { repository.removeMovieFromWishlist(movieId) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { repository.removeMovieFromWishlist(movieId) } throws exception

        try {
            useCase.invoke(movieId)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { repository.removeMovieFromWishlist(movieId) }
    }
}