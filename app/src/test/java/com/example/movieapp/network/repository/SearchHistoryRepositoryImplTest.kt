package com.example.movieapp.network.repository

import app.cash.turbine.test
import com.example.movieapp.core.database.mapper.asSearchEntity
import com.example.movieapp.core.database.mapper.asSearchModel
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.database.source.SearchHistoryDatabaseSource
import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.repository.SearchHistoryRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@RunWith(MockitoJUnitRunner::class)
class SearchHistoryRepositoryImplTest {

    private lateinit var repository: SearchHistoryRepositoryImpl

    @Mock
    private lateinit var searchDatabaseSource: SearchHistoryDatabaseSource

    @Before
    fun setUp() {
        repository = SearchHistoryRepositoryImpl(searchDatabaseSource)
    }

    @Test
    fun `getSearchHistory should emit loading and success states`() = runTest {
        val mockEntities = listOf(
            SearchEntity(
                id = 0,
                mediaType ="",
                title = "Title",
                overview = "",
                popularity = 0.0,
                releaseDate = "",
                adult = false,
                genreEntities = emptyList(),
                originalTitle = "",
                originalLanguage = "",
                voteAverage = 0.0,
                voteCount = 0,
                posterPath = "",
                backdropPath = "",
                video = false,
                rating = 0.0,
                runtime = 0,
                timestamp = 0
            ),
            SearchEntity(
                id = 1,
                mediaType ="",
                title = "Title 2",
                overview = "",
                popularity = 0.0,
                releaseDate = "",
                adult = false,
                genreEntities = emptyList(),
                originalTitle = "",
                originalLanguage = "",
                voteAverage = 0.0,
                voteCount = 0,
                posterPath = "",
                backdropPath = "",
                video = false,
                rating = 0.0,
                runtime = 0,
                timestamp = 0
            ),
        )
        val mockModels = mockEntities.map { it.asSearchModel() }

        whenever(searchDatabaseSource.getSearchHistory()).thenReturn(flowOf(mockEntities))

        repository.getSearchHistory().test {
            assertEquals(CinemaxResponse.Loading, awaitItem())
            assertEquals(CinemaxResponse.Success(mockModels), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getSearchHistory should emit failure when exception occurs`() = runTest {
        whenever(searchDatabaseSource.getSearchHistory()).thenThrow(RuntimeException("Database Error"))

        repository.getSearchHistory().test {
            assertEquals(CinemaxResponse.Loading, awaitItem())
            assertTrue(awaitItem() is CinemaxResponse.Failure)
            awaitComplete()
        }
    }

    @Test
    fun `addMovieToSearchHistory should call database source`() = runTest {
        val searchModel = SearchModel(1, "Movie 1")

        repository.addMovieToSearchHistory(searchModel)

        verify(searchDatabaseSource).addMovieToSearchHistory(searchModel.asSearchEntity())
    }

    @Test
    fun `removeMovieFromSearchHistory should call database source`() = runTest {
        val movieId = 1

        repository.removeMovieFromSearchHistory(movieId)

        verify(searchDatabaseSource).deleteMovieToSearchHistory(movieId)
    }

    @Test
    fun `isSearchExist should return expected result`() = runTest {
        val movieId = 1
        whenever(searchDatabaseSource.isSearchExist(movieId)).thenReturn(true)

        val result = repository.isSearchExist(movieId)

        assertTrue(result)
        verify(searchDatabaseSource).isSearchExist(movieId)
    }
}
