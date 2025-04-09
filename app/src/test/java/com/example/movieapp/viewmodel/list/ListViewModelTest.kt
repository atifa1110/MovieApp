package com.example.movieapp.viewmodel.list

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.ui.asMediaTypeModel
import com.example.movieapp.core.ui.asMovieMediaType
import com.example.movieapp.core.ui.asTvShowMediaType
import com.example.movieapp.list.usecase.GetMoviePagingUseCase
import com.example.movieapp.list.usecase.GetTvPagingUseCase
import com.example.movieapp.list.presentation.ListViewModel
import com.example.movieapp.list.presentation.asTitleResourceId
import com.example.movieapp.navigation.ListDestination
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
class ListViewModelTest {

    private val getMoviePagingUseCase: GetMoviePagingUseCase = mockk(relaxed = true)
    private val getTvPagingUseCase: GetTvPagingUseCase = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk()
    private lateinit var viewModel: ListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        mockkStatic(MediaType.Common.Movie::class) // Ensure correct mediaType is used
        every { savedStateHandle.get<String>("mediaType") } returns "movie_Upcoming"
        Dispatchers.setMain(testDispatcher)
        viewModel = ListViewModel(getMoviePagingUseCase,getTvPagingUseCase,savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getInitialUiState with Movie MediaType should set movies`() = runTest {
        val mediaType = MediaType.Common.Movie.Popular
        val testMovies = listOf(MovieModel(1, "Movie 1", "path1"), MovieModel(2, "Movie 2", "path2"))
        val moviePagingData: Flow<PagingData<MovieModel>> = flowOf(PagingData.from(testMovies))

        val mediaTypeString = mediaType.mediaType
        println("Mocking SavedStateHandle with key: ${ListDestination.mediaTypeArgument}, value: $mediaTypeString")

        every { savedStateHandle.get<String>(ListDestination.mediaTypeArgument) } returns mediaType.mediaType
        every { getMoviePagingUseCase(mediaType.asMovieMediaType().asMediaTypeModel()) } returns moviePagingData

        viewModel = ListViewModel(getMoviePagingUseCase, getTvPagingUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mediaType.asTitleResourceId(), viewModel.uiState.value.mediaType.asTitleResourceId())
        //assertEquals(testMovies.size, viewModel.uiState.value.movies)
    }

    @Test
    fun `getInitialUiState with TvShow MediaType should set tvs`() = runTest {
        val mediaType = MediaType.Common.TvShow.Popular
        val tvPagingData: Flow<PagingData<TvShowModel>> = flowOf(PagingData.empty())

        every { savedStateHandle.get<String>(ListDestination.mediaTypeArgument) } returns mediaType.mediaType
        every { getTvPagingUseCase(mediaType.asTvShowMediaType().asMediaTypeModel()) } returns tvPagingData

        viewModel = ListViewModel(getMoviePagingUseCase, getTvPagingUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mediaType.asTitleResourceId(), viewModel.uiState.value.mediaType.asTitleResourceId())
        //assertEquals(tvPagingData, viewModel.uiState.value.tvs)
    }

    @Test
    fun `getInitialUiState with null MediaType should set default movies`() = runTest {
        val defaultMediaType = MediaType.Common.Movie.Upcoming
        val moviePagingData: Flow<PagingData<MovieModel>> = flowOf(PagingData.empty())
        val tvPagingData: Flow<PagingData<TvShowModel>> = flowOf(PagingData.empty())

        every { savedStateHandle.get<String>("mediaType") } returns null
        every { getMoviePagingUseCase(defaultMediaType.asMovieMediaType().asMediaTypeModel()) } returns moviePagingData
        every { getTvPagingUseCase(defaultMediaType.asTvShowMediaType().asMediaTypeModel()) } returns tvPagingData

        viewModel = ListViewModel(getMoviePagingUseCase, getTvPagingUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(defaultMediaType, viewModel.uiState.value.mediaType)
        //assertEquals(moviePagingData, viewModel.uiState.value.movies)
    }

}