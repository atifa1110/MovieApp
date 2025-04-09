package com.example.movieapp.usecase.detail

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.detail.usecase.AddMovieToWishlistUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class AddMovieToWishlistUseCaseTest {

    private lateinit var repository: WishListRepository
    private lateinit var useCase: AddMovieToWishlistUseCase
    private lateinit var movieDetailModel: MovieDetailModel

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddMovieToWishlistUseCase(repository)
        movieDetailModel = MovieDetailModel(
            1, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)// Example MovieDetailModel
    }

    @Test
    fun `invoke should call addMovieToWishlist with correct MovieDetailModel`() = runBlocking {
        coEvery { repository.addMovieToWishlist(movieDetailModel) } returns Unit

        useCase.invoke(movieDetailModel)

        coVerify(exactly = 1) { repository.addMovieToWishlist(movieDetailModel) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { repository.addMovieToWishlist(movieDetailModel) } throws exception

        try {
            useCase.invoke(movieDetailModel)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { repository.addMovieToWishlist(movieDetailModel) }
    }
}