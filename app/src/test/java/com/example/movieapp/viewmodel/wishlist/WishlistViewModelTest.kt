package com.example.movieapp.viewmodel.wishlist

import android.content.Context
import com.example.movieapp.R
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.asWishlist
import com.example.movieapp.wishlist.presentation.WishlistViewModel
import com.example.movieapp.wishlist.usecase.DeleteMovieFromWishlistUseCase
import com.example.movieapp.wishlist.usecase.DeleteTvShowFromWishlistUseCase
import com.example.movieapp.wishlist.usecase.GetWishlistUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private val getWishlistUseCase: GetWishlistUseCase = mockk(relaxed = true)
    private val deleteMovieFromWishlistUseCase: DeleteMovieFromWishlistUseCase = mockk()
    private val deleteTvShowFromWishlistUseCase: DeleteTvShowFromWishlistUseCase = mockk()
    private lateinit var viewModel: WishlistViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        Dispatchers.setMain(testDispatcher)
        viewModel = WishlistViewModel(getWishlistUseCase, deleteMovieFromWishlistUseCase, deleteTvShowFromWishlistUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWishlist with success should update state`() = runTest {
        val wishlistModels = listOf(WishlistModel(1, MediaTypeModel.Wishlist.Movie, "Movie",
            listOf(GenreModel.ACTION,GenreModel.ACTION_ADVENTURE),
            4.2, "Path",true),WishlistModel(2, MediaTypeModel.Wishlist.TvShow, "TvShow",
            listOf(GenreModel.ACTION,GenreModel.ACTION_ADVENTURE),
            4.2, "Path",true))
        val wishlists = wishlistModels.map(WishlistModel::asWishlist)
        coEvery { getWishlistUseCase.invoke() } returns flowOf(CinemaxResponse.Success(wishlistModels))

        viewModel = WishlistViewModel(getWishlistUseCase, deleteMovieFromWishlistUseCase, deleteTvShowFromWishlistUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(wishlists, viewModel.uiState.value.wishlist)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadWishlist with loading should update state`() = runTest {
        coEvery { getWishlistUseCase.invoke() } returns flowOf(CinemaxResponse.Loading)

        viewModel = WishlistViewModel(getWishlistUseCase, deleteMovieFromWishlistUseCase, deleteTvShowFromWishlistUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `deleteMovieFromWishlist should remove only the specified item and show snackbar`() = runTest {
        val wishlistModels = listOf(
            WishlistModel(1, MediaTypeModel.Wishlist.Movie, "Movie",
                listOf(GenreModel.ACTION, GenreModel.ACTION_ADVENTURE),
                4.2, "Path", true),
            WishlistModel(2, MediaTypeModel.Wishlist.TvShow, "TvShow",
                listOf(GenreModel.ACTION, GenreModel.ACTION_ADVENTURE),
                4.2, "Path", true)
        )
        val remainingWishlist = listOf(wishlistModels[1]) // Expect only the second item to remain

        coEvery { getWishlistUseCase.invoke() } returns flowOf(CinemaxResponse.Success(wishlistModels))
        coEvery { deleteMovieFromWishlistUseCase.invoke(1) } returns Unit
        coEvery { getWishlistUseCase.invoke() } returns flowOf(CinemaxResponse.Success(remainingWishlist))

        viewModel = WishlistViewModel(getWishlistUseCase, deleteMovieFromWishlistUseCase, deleteTvShowFromWishlistUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.deleteMovieFromWishlist(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that only the second item remains
        assertEquals(remainingWishlist.map(WishlistModel::asWishlist), viewModel.uiState.value.wishlist)

        // ✅ Convert resource ID to actual string before asserting
        val expectedMessage = R.string.success_delete_from_wishlist
        val actualMessage = viewModel.uiState.value.userMessage.value

        coVerify { deleteMovieFromWishlistUseCase.invoke(1) }
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `deleteMovieFromWishlist should remove empty call use case and show snackbar`() = runTest {
        val id = 1
        coEvery { deleteMovieFromWishlistUseCase.invoke(id) } returns Unit
        coEvery { getWishlistUseCase.invoke() } returns flowOf(CinemaxResponse.Success(emptyList()))

        viewModel.deleteMovieFromWishlist(id)
        testDispatcher.scheduler.advanceUntilIdle()

        // ✅ Convert resource ID to actual string before asserting
        val expectedMessage = R.string.success_delete_from_wishlist
        val actualMessage = viewModel.uiState.value.userMessage.value

        coVerify { deleteMovieFromWishlistUseCase.invoke(id) }
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `snackBarMessageShown should clear userMessage`() = runTest {
        viewModel.uiState.value.userMessage.value = R.string.success_delete_from_wishlist
        viewModel.snackBarMessageShown()

        assertNull(viewModel.uiState.value.userMessage.value)
    }
}