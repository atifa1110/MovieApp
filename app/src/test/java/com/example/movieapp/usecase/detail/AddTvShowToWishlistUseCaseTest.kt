package com.example.movieapp.usecase.detail

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.detail.usecase.AddTvShowToWishlistUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class AddTvShowToWishlistUseCaseTest {
    private lateinit var repository: WishListRepository
    private lateinit var useCase: AddTvShowToWishlistUseCase
    private lateinit var tvShowDetailModel: TvShowDetailModel

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddTvShowToWishlistUseCase(repository)
        tvShowDetailModel = TvShowDetailModel(1, "Title", false,"", emptyList(),"",
            emptyList(), emptyList(),"",false, emptyList(),"",0, 0, emptyList(),"","","overview",0.0,
            "","","","",0.0, 0, CreditsModel(emptyList(), emptyList()),
            0.0,  false)
    }

    @Test
    fun `invoke should call addTvShowToWishlist with correct TvShowDetailModel`() = runBlocking {
        coEvery { repository.addTvShowToWishlist(tvShowDetailModel) } returns Unit

        useCase.invoke(tvShowDetailModel)

        coVerify(exactly = 1) { repository.addTvShowToWishlist(tvShowDetailModel) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { repository.addTvShowToWishlist(tvShowDetailModel) } throws exception

        try {
            useCase.invoke(tvShowDetailModel)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { repository.addTvShowToWishlist(tvShowDetailModel) }
    }
}