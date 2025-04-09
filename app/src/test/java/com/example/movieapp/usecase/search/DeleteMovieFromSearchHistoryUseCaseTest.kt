package com.example.movieapp.usecase.search

import com.example.movieapp.core.network.repository.SearchHistoryRepository
import com.example.movieapp.search.usecase.DeleteMovieFromSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class DeleteMovieFromSearchHistoryUseCaseTest {

    private var searchRepository: SearchHistoryRepository = mockk()
    private lateinit var useCase: DeleteMovieFromSearchHistoryUseCase
    private val movieId = 123

    @Before
    fun setup() {
        useCase = DeleteMovieFromSearchHistoryUseCase(searchRepository)
    }

    @Test
    fun `invoke should call removeMovieFromSearchHistory with correct id`() = runBlocking {
        coEvery { searchRepository.removeMovieFromSearchHistory(movieId) } returns Unit

        useCase.invoke(movieId)

        coVerify(exactly = 1) { searchRepository.removeMovieFromSearchHistory(movieId) }
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        val exception = Exception("Repository error")

        coEvery { searchRepository.removeMovieFromSearchHistory(movieId) } throws exception

        try {
            useCase.invoke(movieId)
            assertEquals(true, false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }

        coVerify(exactly = 1) { searchRepository.removeMovieFromSearchHistory(movieId) }
    }
}