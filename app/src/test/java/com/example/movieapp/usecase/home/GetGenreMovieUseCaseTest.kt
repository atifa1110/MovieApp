package com.example.movieapp.usecase.home

import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.home.usecase.GetGenreMovieUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetGenreMovieUseCaseTest{

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetGenreMovieUseCase
    private lateinit var genreModels: List<GenreModel>

    @Before
    fun setup() {
        movieRepository = mockk()
        useCase = GetGenreMovieUseCase(movieRepository)
        genreModels = listOf(
            GenreModel(1, "action"),
        )
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { movieRepository.genreMovie() } returns flowOf(CinemaxResponse.Loading)

        val result = useCase.invoke().toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<List<GenreModel>> = CinemaxResponse.Success(genreModels)
        every { movieRepository.genreMovie() } returns flowOf(successResponse)

        val result: List<CinemaxResponse<List<GenreModel>>> = useCase.invoke().toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<List<GenreModel>> = CinemaxResponse.Failure(errorMessage)
        every { movieRepository.genreMovie() } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<List<GenreModel>>> = useCase.invoke().toList()

        assertEquals(listOf(errorResponse), result)
    }
}