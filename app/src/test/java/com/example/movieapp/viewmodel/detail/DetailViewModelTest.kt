package com.example.movieapp.viewmodel.detail

import androidx.lifecycle.SavedStateHandle
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.R
import com.example.movieapp.core.ui.asMovieDetails
import com.example.movieapp.core.ui.asTvShowDetails
import com.example.movieapp.detail.presentation.DetailViewModel
import com.example.movieapp.detail.presentation.DetailsEvent
import com.example.movieapp.detail.usecase.AddMovieToWishlistUseCase
import com.example.movieapp.detail.usecase.AddTvShowToWishlistUseCase
import com.example.movieapp.detail.usecase.GetDetailMovieUseCase
import com.example.movieapp.detail.usecase.GetDetailTvShowUseCase
import com.example.movieapp.detail.usecase.RemoveMovieFromWishlistUseCase
import com.example.movieapp.detail.usecase.RemoveTvShowFromWishlistUseCase
import com.example.movieapp.navigation.DetailsDestination
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest{

    private val addMovieToWishlistUseCase: AddMovieToWishlistUseCase = mockk(relaxed = true)
    private val removeMovieFromWishlistUseCase: RemoveMovieFromWishlistUseCase = mockk(relaxed = true)
    private val addTvShowToWishlistUseCase: AddTvShowToWishlistUseCase = mockk(relaxed = true)
    private val removeTvShowFromWishlistUseCase: RemoveTvShowFromWishlistUseCase = mockk(relaxed = true)
    private val getDetailMovieUseCase: GetDetailMovieUseCase = mockk(relaxed = true)
    private val getDetailTvShowUseCase: GetDetailTvShowUseCase = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns 1
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(addMovieToWishlistUseCase, removeMovieFromWishlistUseCase, addTvShowToWishlistUseCase, removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase, getDetailTvShowUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovie should update uiState with movie details on success`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)

        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(movieDetails.asMovieDetails(), viewModel.uiState.value.movie)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadMovie should update uiState with movie trailers details on success`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)

        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "trailers"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(movieDetails.asMovieDetails(), viewModel.uiState.value.movie)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadTv should update uiState with tv show details on success`() = runTest {
        val tvShowId = 1
        val tvShowDetails = TvShowDetailModel(tvShowId, "Title", false,"", emptyList(),"",
            emptyList(), emptyList(),"",false, emptyList(),"",0, 0, emptyList(),"","","overview",0.0,
            "","","","",0.0, 0, CreditsModel(emptyList(), emptyList()),
            0.0,  false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns tvShowId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "tv_show"
        every { getDetailTvShowUseCase(tvShowId) } returns flowOf(CinemaxResponse.Success(tvShowDetails))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(tvShowDetails.asTvShowDetails(), viewModel.uiState.value.tvShow)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadMovie should update uiState with error message on failure`() = runTest {
        val movieId = 1
        val errorMessage = "Network error"
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadMovieTrailers should update uiState with error message on failure`() = runTest {
        val movieId = 1
        val errorMessage = "Network error"
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "trailers"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadTv should update uiState with error message on failure`() = runTest {
        val tvShowId = 1
        val errorMessage = "Network error"
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns tvShowId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "tv_show"
        every { getDetailTvShowUseCase(tvShowId) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onWishlistMovie should add movie to wishlist and update uiState`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))
        coEvery { addMovieToWishlistUseCase(movieDetails) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistMovie)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.movie?.isWishListed ?: false)
        assertEquals(R.string.add_wishlist, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `onWishlistMovie should remove movie from wishlist and update uiState`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),true)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))
        coEvery { removeMovieFromWishlistUseCase(movieId) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase, removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase, removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase, getDetailTvShowUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistMovie)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.movie?.isWishListed ?: false)
        assertEquals(R.string.remove_wishlist, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `onWishlistTvShow should add tv show to wishlist and update uiState`() = runTest {
        val tvShowId = 1
        val tvShowDetails = TvShowDetailModel(
            tvShowId, "Title", false, "", emptyList(), "",
            emptyList(), emptyList(), "", false, emptyList(), "", 0, 0, emptyList(), "", "", "overview", 0.0,
            "", "", "", "", 0.0, 0, CreditsModel(emptyList(), emptyList()),
            0.0, false
        )
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns tvShowId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "tv_show"
        every { getDetailTvShowUseCase(tvShowId) } returns flowOf(CinemaxResponse.Success(tvShowDetails))
        coEvery { addTvShowToWishlistUseCase(tvShowDetails) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistTv)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.tvShow?.isWishListed ?: false)
        assertEquals(R.string.add_wishlist, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `onWishlistTvShow should remove tv show from wishlist and update uiState`() = runTest {
        val tvShowId = 1
        val tvShowDetails = TvShowDetailModel(
            tvShowId, "Title", false, "", emptyList(), "",
            emptyList(), emptyList(), "", false, emptyList(), "", 0, 0, emptyList(), "", "", "overview", 0.0,
            "", "", "", "", 0.0, 0, CreditsModel(emptyList(), emptyList()),
            0.0, true
        )
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns tvShowId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "tv_show"
        every { getDetailTvShowUseCase(tvShowId) } returns flowOf(CinemaxResponse.Success(tvShowDetails))
        coEvery { removeTvShowFromWishlistUseCase(tvShowId) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistTv)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.tvShow?.isWishListed ?: false)
        assertEquals(R.string.remove_wishlist, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `onRefresh should reload content`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(movieDetails.asMovieDetails(), viewModel.uiState.value.movie)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onRetry should clear error and reload content`() = runTest {
        val movieId = 1
        val errorMessage = "Network error"
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Trigger an error state
        viewModel.onEvent(DetailsEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)

        // Retry and check if error is cleared
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))
        viewModel.onEvent(DetailsEvent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals("title", viewModel.uiState.value.movie?.title)
    }

    @Test
    fun `onClearUserMessage clears userMessage`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))
        coEvery { addMovieToWishlistUseCase(movieDetails) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistMovie)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.movie?.isWishListed ?: false)
        assertEquals(R.string.add_wishlist, viewModel.uiState.value.userMessage)

        viewModel.onEvent(DetailsEvent.ClearUserMessage)
        assertNull(viewModel.uiState.value.userMessage)
    }

    @Test
    fun `snackBarMessageShown clears userMessage`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailModel(
            movieId, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
        every { savedStateHandle.get<Int>(DetailsDestination.idArgument) } returns movieId
        every { savedStateHandle.get<String>(DetailsDestination.mediaTypeArgument) } returns "movie"
        every { getDetailMovieUseCase(movieId) } returns flowOf(CinemaxResponse.Success(movieDetails))
        coEvery { addMovieToWishlistUseCase(movieDetails) } returns Unit

        viewModel = DetailViewModel(
            addMovieToWishlistUseCase,
            removeMovieFromWishlistUseCase,
            addTvShowToWishlistUseCase,
            removeTvShowFromWishlistUseCase,
            getDetailMovieUseCase,
            getDetailTvShowUseCase,
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(DetailsEvent.WishlistMovie)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(R.string.add_wishlist, viewModel.uiState.value.userMessage)

        viewModel.snackBarMessageShown()
        assertNull(viewModel.uiState.value.userMessage)
    }
}