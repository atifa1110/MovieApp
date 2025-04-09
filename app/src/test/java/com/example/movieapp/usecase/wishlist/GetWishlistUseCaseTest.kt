package com.example.movieapp.usecase.wishlist

import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.wishlist.usecase.GetWishlistUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetWishlistUseCaseTest {


    private lateinit var wishlistRepository: WishListRepository
    private lateinit var useCase: GetWishlistUseCase
    private lateinit var wishlistModels: List<WishlistModel>

    @Before
    fun setup() {
        wishlistRepository = mockk()
        useCase = GetWishlistUseCase(wishlistRepository)
        wishlistModels = listOf(
            WishlistModel(1, MediaTypeModel.Wishlist.Movie, "Movie",
                listOf(GenreModel.ACTION, GenreModel.ACTION_ADVENTURE),0.0,"",false),
            WishlistModel(2, MediaTypeModel.Wishlist.TvShow, "TvShow",
                listOf(GenreModel.ACTION,GenreModel.ACTION_ADVENTURE),0.0,"",false),
        )
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { wishlistRepository.getWishlist() } returns flowOf(CinemaxResponse.Loading)

        val result = useCase.invoke().toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<List<WishlistModel>> = CinemaxResponse.Success(wishlistModels)
        every { wishlistRepository.getWishlist() } returns flowOf(successResponse)

        val result: List<CinemaxResponse<List<WishlistModel>>> = useCase.invoke().toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<List<WishlistModel>> = CinemaxResponse.Failure(errorMessage)
        every { wishlistRepository.getWishlist() } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<List<WishlistModel>>> = useCase.invoke().toList()

        assertEquals(listOf(errorResponse), result)
    }
}