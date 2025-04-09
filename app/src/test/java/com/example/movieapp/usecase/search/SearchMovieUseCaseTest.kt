package com.example.movieapp.usecase.search

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.search.usecase.SearchMovieUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchMovieUseCaseTest{

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: SearchMovieUseCase
    private val query = "test query"
    private lateinit var pagingData: PagingData<MovieModel>

    @Before
    fun setup() {
        movieRepository = mockk()
        useCase = SearchMovieUseCase(movieRepository)
        pagingData = PagingData.from(listOf(MovieModel(1, "Movie 1", "poster1.jpg"))) // Example PagingData
    }

    @Test
    fun `invoke should return Flow of PagingData from repository`() = runBlocking {
        every { movieRepository.searchMovie(query) } returns flowOf(pagingData)

        val result  = useCase.invoke(query)

        result.collect { actualPagingData ->
            assertEquals(pagingData, actualPagingData)
        }
    }

    @Test
    fun `invoke should handle repository errors`() = runBlocking {
        val errorPagingData: PagingData<MovieModel> = PagingData.empty() //Example empty paging data on error.
        every { movieRepository.searchMovie(query) } returns flowOf(errorPagingData)

        val result = useCase.invoke(query)

        result.collect { actualPagingData ->
            assertEquals(errorPagingData, actualPagingData)
        }
    }
}