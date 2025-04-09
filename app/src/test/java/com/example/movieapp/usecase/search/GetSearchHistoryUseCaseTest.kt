package com.example.movieapp.usecase.search

import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.repository.SearchHistoryRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.search.usecase.GetSearchHistoryUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetSearchHistoryUseCaseTest{

    private lateinit var searchRepository: SearchHistoryRepository
    private lateinit var useCase: GetSearchHistoryUseCase
    private lateinit var searchModels: List<SearchModel>

    @Before
    fun setup() {
        searchRepository = mockk()
        useCase = GetSearchHistoryUseCase(searchRepository)
        searchModels = listOf(
            SearchModel(1, "Movie 1", "poster1.jpg"),
            SearchModel(2, "Movie 2", "poster2.jpg")
        )
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { searchRepository.getSearchHistory() } returns flowOf(CinemaxResponse.Loading)

        val result  = useCase.invoke().toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<List<SearchModel>> = CinemaxResponse.Success(searchModels)
        every { searchRepository.getSearchHistory() } returns flowOf(successResponse)

        val result = useCase.invoke().toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<List<SearchModel>> = CinemaxResponse.Failure(errorMessage)
        every { searchRepository.getSearchHistory() } returns flowOf(errorResponse)

        val result = useCase.invoke().toList()

        assertEquals(listOf(errorResponse), result)
    }
}