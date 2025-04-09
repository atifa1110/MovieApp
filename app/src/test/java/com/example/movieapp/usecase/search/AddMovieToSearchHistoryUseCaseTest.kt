package com.example.movieapp.usecase.search

import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.repository.SearchHistoryRepository
import com.example.movieapp.search.usecase.AddMovieToSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddMovieToSearchHistoryUseCaseTest{

    private lateinit var searchRepository: SearchHistoryRepository
    private lateinit var useCase: AddMovieToSearchHistoryUseCase
    private lateinit var searchModel: SearchModel

    @Before
    fun setup() {
        searchRepository = mockk()
        useCase = AddMovieToSearchHistoryUseCase(searchRepository)
        searchModel = SearchModel(1, "Test Movie", "test_poster.jpg") // Example SearchModel
    }

    @Test
    fun `invoke should call addMovieToSearchHistory with correct searchModel`() = runBlocking {
        coEvery { searchRepository.addMovieToSearchHistory(searchModel) } returns Unit

        useCase.invoke(searchModel)

        coVerify(exactly = 1) { searchRepository.addMovieToSearchHistory(searchModel) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { searchRepository.addMovieToSearchHistory(searchModel) } throws exception

        try {
            useCase.invoke(searchModel)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { searchRepository.addMovieToSearchHistory(searchModel) }
    }
}