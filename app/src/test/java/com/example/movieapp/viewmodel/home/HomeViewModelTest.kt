package com.example.movieapp.viewmodel.home

import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.asGenreNames
import com.example.movieapp.core.ui.asMediaTypeModel
import com.example.movieapp.home.usecase.GetGenreMovieUseCase
import com.example.movieapp.home.usecase.GetTvShowUseCase
import com.example.movieapp.home.usecase.GetUserId
import com.example.movieapp.home.presentation.HomeEvent
import com.example.movieapp.home.presentation.HomeViewModel
import com.example.movieapp.search.usecase.GetMovieUseCase
import com.google.firebase.auth.FirebaseUser
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
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getUserId: GetUserId = mockk(relaxed = true)
    private val getMovieUseCase: GetMovieUseCase = mockk(relaxed = true)
    private val getGenreMovieUseCase: GetGenreMovieUseCase = mockk(relaxed = true)
    private val getTvShowUseCase: GetTvShowUseCase = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        Dispatchers.setMain(testDispatcher)
        every { getUserId() } returns mockk<FirebaseUser>()
        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadGenre with success should update state`() = runTest {
        val genreModels = listOf(GenreModel(1, "Action"), GenreModel(2, "Adventure"))
        coEvery { getGenreMovieUseCase.invoke() } returns flowOf(CinemaxResponse.Success(genreModels))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(genreModels.asGenreNames(), viewModel.uiState.value.genres)
        assertFalse(viewModel.uiState.value.loadGenre)
    }

    @Test
    fun `loadGenre with loading should update state`() = runTest {
        coEvery { getGenreMovieUseCase.invoke() } returns flowOf(CinemaxResponse.Loading)

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loadGenre)
    }

    @Test
    fun `loadGenre with failure should update state`() = runTest {
        val errorMessage = "Failed to load genres"
        coEvery { getGenreMovieUseCase.invoke() } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loadGenre)
    }

    @Test
    fun `loadMovies with success should update state`() = runTest {
        val mediaType = MediaType.Movie.Upcoming
        val movies = listOf(MovieModel(1, "Movie 1"), MovieModel(2, "Movie 2"))
        coEvery { getMovieUseCase(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Success(movies))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(movies.size, viewModel.uiState.value.movies[mediaType]?.size)
        assertFalse(viewModel.uiState.value.loadStates[mediaType] ?: true)
    }

    @Test
    fun `loadMovies with loading should update state`() = runTest {
        val mediaType = MediaType.Movie.NowPlaying
        coEvery { getMovieUseCase(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Loading)

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loadStates[mediaType] ?: false)
    }

    @Test
    fun `loadMovies with failure should update state`() = runTest {
        val mediaType = MediaType.Movie.Popular
        val errorMessage = "Failed to load movies"
        coEvery { getMovieUseCase(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loadStates[mediaType] ?: true)
    }

    @Test
    fun `loadTvShow with success should update state`() = runTest {
        val mediaType = MediaType.TvShow.Popular
        val tvShows = listOf(TvShowModel(1, "Tv Show 1","",0.0,"", emptyList(),"","", emptyList(),0.0,0,"","",0.0),
            TvShowModel(2, "Tv Show 2","",0.0,"", emptyList(),"","", emptyList(),0.0,0,"","",0.0))
        coEvery { getTvShowUseCase.invoke(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Success(tvShows))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(tvShows.size, viewModel.uiState.value.tvShows[mediaType]?.size)
        assertFalse(viewModel.uiState.value.loadStates[mediaType] ?: true)
    }

    @Test
    fun `loadTvShow with loading should update state`() = runTest {
        val mediaType = MediaType.TvShow.NowPlaying
        coEvery { getTvShowUseCase.invoke(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Loading)

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loadStates[mediaType] ?: false)
    }

    @Test
    fun `loadTvShow with failure should update state`() = runTest {
        val mediaType = MediaType.TvShow.Popular
        val errorMessage = "Failed to load TV shows"
        coEvery { getTvShowUseCase.invoke(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Failure(errorMessage))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loadStates[mediaType] ?: true)
    }

    @Test
    fun `onCategorySelected should update selectedCategory`() {
        val category = "Drama"
        viewModel.onCategorySelected(category)
        assertEquals(category, viewModel.uiState.value.selectedCategory)
    }

    @Test
    fun `onRefresh should reload content`() = runTest{
        val mediaType = MediaType.Movie.Upcoming
        val movies = listOf(MovieModel(1, "Movie 1"), MovieModel(2, "Movie 2"))
        coEvery { getMovieUseCase(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Success(movies))

        viewModel = HomeViewModel(getUserId, getMovieUseCase, getGenreMovieUseCase, getTvShowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(HomeEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(movies.size, viewModel.uiState.value.movies[mediaType]?.size)
    }

    @Test
    fun `onRetry should clear error and refresh`() = runTest{
        val mediaType = MediaType.Movie.Upcoming
        val movies = listOf(MovieModel(1, "Movie 1"), MovieModel(2, "Movie 2"))
        coEvery { getMovieUseCase(mediaType.asMediaTypeModel()) } returns flowOf(CinemaxResponse.Success(movies))
        viewModel.uiState.value.copy(errorMessage = "Test Error")

        viewModel.onEvent(HomeEvent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(null, viewModel.uiState.value.errorMessage)
        assertEquals(movies.size, viewModel.uiState.value.movies[mediaType]?.size)
    }

    @Test
    fun `onClearError should clear errorMessage`() {
        viewModel.uiState.value.copy(errorMessage = "Test Error")
        viewModel.onEvent(HomeEvent.ClearError)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }
}