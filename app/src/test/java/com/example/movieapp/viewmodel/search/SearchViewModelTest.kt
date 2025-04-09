package com.example.movieapp.viewmodel.search

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.asMovie
import com.example.movieapp.core.ui.asSearchModel
import com.example.movieapp.home.usecase.GetTvShowUseCase
import com.example.movieapp.search.presentation.SearchEvent
import com.example.movieapp.search.presentation.SearchViewModel
import com.example.movieapp.search.usecase.AddMovieToSearchHistoryUseCase
import com.example.movieapp.search.usecase.DeleteMovieFromSearchHistoryUseCase
import com.example.movieapp.search.usecase.GetMovieUseCase
import com.example.movieapp.search.usecase.GetSearchHistoryUseCase
import com.example.movieapp.search.usecase.SearchMovieUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val searchMovieUseCase: SearchMovieUseCase = mockk(relaxed = true)
    private val addMovieToSearchHistoryUseCase: AddMovieToSearchHistoryUseCase = mockk(relaxed = true)
    private val deleteMovieFromSearchHistoryUseCase: DeleteMovieFromSearchHistoryUseCase = mockk(relaxed = true)
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase = mockk(relaxed = true)
    private val getMovieUseCase: GetMovieUseCase = mockk(relaxed = true)
    private val getTvShowUseCase: GetTvShowUseCase = mockk(relaxed = true)

    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(
            searchMovieUseCase, addMovieToSearchHistoryUseCase,
            deleteMovieFromSearchHistoryUseCase, getSearchHistoryUseCase,
            getMovieUseCase, getTvShowUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryChange should update query and isSearching state`() = runTest {
        val query = "test query"
        viewModel.onEvent(SearchEvent.ChangeQuery(query))
        assertEquals(query, viewModel.uiState.value.query)
        assertTrue(viewModel.uiState.value.isSearching)
    }

    @Test
    fun `onQueryChange with empty query should set isSearching to false`() = runTest {
        val query = ""
        viewModel.onEvent(SearchEvent.ChangeQuery(query))
        assertEquals(query, viewModel.uiState.value.query)
        assertFalse(viewModel.uiState.value.isSearching)
    }

//    @Test
//    fun `search should update searchMovies state`() = runTest {
//        val query = "test"
//        val movieModel = MovieModel(
//            1, "test title", "overview", 0.0, "", false,
//            emptyList(), "", "", 0.0, 0, "poster", "profile",
//            "backdrop", false, 8.0, "releaseDate", 100
//        )
//
//        val listMovie = listOf(movieModel)
//        val pagingData = PagingData.from(listMovie)
//        every { searchMovieUseCase(query) } returns flowOf(pagingData)
//
//        viewModel.onEvent(SearchEvent.ChangeQuery(query))
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val result = viewModel.uiState.value.searchMovies.toList().asPagingSourceFactory()
//        val pagingSource = result.invoke()
//
//        val loadResult = pagingSource.load(
//            PagingSource.LoadParams.Refresh(
//                key = null, // First page
//                loadSize = 20, // Page size
//                placeholdersEnabled = false
//            )
//        )
//
//        val movies = (loadResult as? PagingSource.LoadResult.Page)?.data ?: emptyList()
//
//        assertEquals(1,movies.size)
//    }

    @Test
    fun `addSearchHistory should add movie to history and update history state`() = runTest {
        val movie = MovieModel(
            1, "title", "overview", 0.0,"",false,
            emptyList(),"","",0.0,0,"poster", "profile",
            "backdrop", false,8.0, "releaseDate", 100).asMovie()
        val searchModel = movie.asSearchModel()
        coEvery { addMovieToSearchHistoryUseCase(searchModel) } returns Unit
        every { getSearchHistoryUseCase() } returns flowOf(CinemaxResponse.Success(listOf(searchModel)))

        viewModel.addSearchHistory(movie)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Success Add to History", viewModel.uiState.value.userMessage.value)
        assertFalse(viewModel.uiState.value.historyUiState.isHistory)
        assertEquals(1, viewModel.uiState.value.historyUiState.historyMovies.size)
    }

    @Test
    fun `deleteSearchHistory should delete movie from history and update history state`() = runTest {
        val movieId = 1
        coEvery { deleteMovieFromSearchHistoryUseCase(movieId) } returns Unit
        every { getSearchHistoryUseCase() } returns flowOf(CinemaxResponse.Success(emptyList()))

        viewModel.deleteSearchHistory(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Success Delete From History", viewModel.uiState.value.userMessage.value)
        assertFalse(viewModel.uiState.value.historyUiState.isHistory)
        assertTrue(viewModel.uiState.value.historyUiState.historyMovies.isEmpty())
    }

    @Test
    fun `loadSearchHistory should update historyUiState`() = runTest {
        val searchModel = SearchModel(1, "title", "poster")
        every { getSearchHistoryUseCase() } returns flowOf(CinemaxResponse.Success(listOf(searchModel)))

        viewModel = SearchViewModel(
            searchMovieUseCase, addMovieToSearchHistoryUseCase,
            deleteMovieFromSearchHistoryUseCase, getSearchHistoryUseCase,
            getMovieUseCase, getTvShowUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.historyUiState.isHistory)
        assertEquals(1, viewModel.uiState.value.historyUiState.historyMovies.size)
    }

    @Test
    fun `loadMovies should update trendingMovieUiState`() = runTest {
        val movieModel = MovieModel(
            1, "title", "overview", 0.0,"",false,
            emptyList(),"","",0.0,0,"poster", "profile",
            "backdrop", false,8.0, "releaseDate", 100)

        every { getMovieUseCase(MediaTypeModel.Movie.Trending) } returns flowOf(CinemaxResponse.Success(listOf(movieModel)))

        viewModel = SearchViewModel(
            searchMovieUseCase, addMovieToSearchHistoryUseCase,
            deleteMovieFromSearchHistoryUseCase, getSearchHistoryUseCase,
            getMovieUseCase, getTvShowUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.trendingMovieUiState.isTrendingMovie)
        assertEquals(1, viewModel.uiState.value.trendingMovieUiState.trendingMovies.size)
    }

    @Test
    fun `loadTv should update trendingTvUiState`() = runTest {
        val tvShowModel = TvShowModel(1, "title", "overview", 0.0,
            "2010", emptyList(),"title","",
            emptyList(),0.0,0, "poster", "backdrop", 8.0,)
        every { getTvShowUseCase(MediaTypeModel.TvShow.Trending) } returns flowOf(CinemaxResponse.Success(listOf(tvShowModel)))

        viewModel = SearchViewModel(
            searchMovieUseCase, addMovieToSearchHistoryUseCase,
            deleteMovieFromSearchHistoryUseCase, getSearchHistoryUseCase,
            getMovieUseCase, getTvShowUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.trendingTvUiState.isTrendingTv)
        assertEquals(1, viewModel.uiState.value.trendingTvUiState.trendingTv.size)
    }

    @Test
    fun `snackBarMessageShown clears userMessage`() = runTest {
        viewModel.uiState.value.userMessage.value = "Test Message"
        viewModel.snackBarMessageShown()
        assertNull(viewModel.uiState.value.userMessage.value)
    }

    @Test
    fun `onClearError clears error state`() = runTest {
        val testError = Throwable("Test error")
        viewModel.uiState.value.copy(error = testError)
        viewModel.onEvent(SearchEvent.ClearError)
        assertNull(viewModel.uiState.value.error)
    }
}