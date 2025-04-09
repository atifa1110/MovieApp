package com.example.movieapp.core.data

import androidx.paging.PagingSource
import com.example.movieapp.core.data.SearchPagingSource
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.network.response.movies.MovieNetwork
import createMockDetailMovieModel
import createMockMovieModel
import createMockMovieResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPagingSourceTest {

    private lateinit var networkDataSource: MovieNetworkDataSource
    private lateinit var searchPagingSource: SearchPagingSource
    private val query = "testQuery"

    @Before
    fun setup() {
        networkDataSource = mock()
        searchPagingSource = SearchPagingSource(query, networkDataSource)
    }

    @Test
    fun `load - success - first page`() = runTest {
        val movieNetworks = listOf(MovieNetwork(
            adult = false, backdropPath = "/backdrop.jpg",
            genreIds = emptyList(), id = 1, originalLanguage = "en", name = "Title 1", originalTitle = "Title 1",
            overview = "Overview 1", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
            mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
            title = "Title 1", video = false, voteAverage = 8.0, voteCount = 100,
        ), MovieNetwork(
            adult = false, backdropPath = "/backdrop.jpg",
            genreIds = emptyList(), id = 2, originalLanguage = "en", name = "Title 2", originalTitle = "Title 2",
            overview = "Overview 2", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
            mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
            title = "Title 2", video = false, voteAverage = 8.0, voteCount = 100,)
        )

        val movieDetail1 = createMockDetailMovieModel(1,120)
        val movieDetail2 = createMockDetailMovieModel(2,130)

        val movieModels = listOf(
            createMockMovieModel(1).copy(runtime = 120),
            createMockMovieModel(2).copy(runtime = 130)
        )

        val mockResponse = createMockMovieResponse(movieNetworks)

        whenever(networkDataSource.search(any(), any())).thenReturn(CinemaxResponse.Success(mockResponse))
        whenever(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Success(movieDetail1))
        whenever(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Success(movieDetail2))

        val expected = PagingSource.LoadResult.Page(
            data = movieModels,
            prevKey = null,
            nextKey = 2
        )

        val result = searchPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `load - success - not first page`() = runTest {
        val movieNetworks = listOf(MovieNetwork(
                adult = false, backdropPath = "/backdrop.jpg",
        genreIds = emptyList(), id = 1, originalLanguage = "en", name = "Title 1", originalTitle = "Title 1",
        overview = "Overview 1", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
        mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
        title = "Title 1", video = false, voteAverage = 8.0, voteCount = 100,
        ), MovieNetwork(
            adult = false, backdropPath = "/backdrop.jpg",
            genreIds = emptyList(), id = 2, originalLanguage = "en", name = "Title 2", originalTitle = "Title 2",
            overview = "Overview 2", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
            mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
            title = "Title 2", video = false, voteAverage = 8.0, voteCount = 100,)
        )

        val movieDetail1 = createMockDetailMovieModel(1,120)
        val movieDetail2 = createMockDetailMovieModel(2,130)

        val movieModels = listOf(
            createMockMovieModel(1).copy(runtime = 120),
            createMockMovieModel(2).copy(runtime = 130)
        )

        val mockResponse = createMockMovieResponse(movieNetworks)

        whenever(networkDataSource.search(any(), any())).thenReturn(CinemaxResponse.Success(mockResponse))
        whenever(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Success(movieDetail1))
        whenever(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Success(movieDetail2))

        val expected = PagingSource.LoadResult.Page(
            data = movieModels,
            prevKey = 1,
            nextKey = 3
        )

        val loadParams = PagingSource.LoadParams.Append(
            key = 2,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val result = searchPagingSource.load(loadParams)

        assertEquals(expected, result)
    }

    @Test
    fun `load - empty results`() = runTest {
        val mockResponse = createMockMovieResponse(emptyList())
        whenever(networkDataSource.search(any(), any())).thenReturn(CinemaxResponse.Success(mockResponse))

        val expected = PagingSource.LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )

        val result = searchPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `load - network failure`() = runTest {
        val errorMessage = "Network error"
        whenever(networkDataSource.search(any(), any())).thenReturn(
            CinemaxResponse.Failure(errorMessage)
        )

        val result = searchPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        assertEquals(errorMessage, (result as PagingSource.LoadResult.Error).throwable.message)
    }

    @Test
    fun `load - detail movie failure`() = runTest {
        val movieNetworks = listOf(MovieNetwork(
            adult = false, backdropPath = "/backdrop.jpg",
            genreIds = emptyList(), id = 1, originalLanguage = "en", name = "Title 1", originalTitle = "Title 1",
            overview = "Overview 1", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
            mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
            title = "Title 1", video = false, voteAverage = 8.0, voteCount = 100,
        ), MovieNetwork(
            adult = false, backdropPath = "/backdrop.jpg",
            genreIds = emptyList(), id = 2, originalLanguage = "en", name = "Title 2", originalTitle = "Title 2",
            overview = "Overview 2", popularity = 7.5, posterPath = "/poster.jpg", profilePath = "/profile.jpg",
            mediaType = "movie", releaseDate = "2023-10-26", firstAirDate = "2023-10-26",
            title = "Title 2", video = false, voteAverage = 8.0, voteCount = 100,)
        )

        val movieModels = listOf(
            createMockMovieModel(1),
            createMockMovieModel(2)
        )

        val mockResponse = createMockMovieResponse(movieNetworks)

        whenever(networkDataSource.search(any(), any())).thenReturn(CinemaxResponse.Success(mockResponse))
        whenever(networkDataSource.getDetailMovie(1)).thenReturn(CinemaxResponse.Failure("Detail Error"))
        whenever(networkDataSource.getDetailMovie(2)).thenReturn(CinemaxResponse.Failure("Detail Error"))

        val expected = PagingSource.LoadResult.Page(
            data = movieModels,
            prevKey = null,
            nextKey = 2
        )

        val result = searchPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertEquals(expected, result)
    }
}