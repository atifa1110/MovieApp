package com.example.movieapp.usecase.detail

import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.detail.usecase.RemoveTvShowFromWishlistUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RemoveTvShowFromWishlistUseCaseTest {

    private lateinit var repository: WishListRepository
    private lateinit var useCase: RemoveTvShowFromWishlistUseCase
    private val tvShowId = 123

    @Before
    fun setup() {
        repository = mockk()
        useCase = RemoveTvShowFromWishlistUseCase(repository)
    }

    @Test
    fun `invoke should call removeTvShowFromWishlist with correct id`() = runBlocking {
        coEvery { repository.removeTvShowFromWishlist(tvShowId) } returns Unit

        useCase.invoke(tvShowId)

        coVerify(exactly = 1) { repository.removeTvShowFromWishlist(tvShowId) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { repository.removeTvShowFromWishlist(tvShowId) } throws exception

        try {
            useCase.invoke(tvShowId)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { repository.removeTvShowFromWishlist(tvShowId) }
    }
}