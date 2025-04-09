package com.example.movieapp.usecase.search

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.search.usecase.GetMovieUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetMovieUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieUseCase
    private lateinit var mediaTypeModel: MediaTypeModel.Movie
    private lateinit var movieModels: List<MovieModel>
    private lateinit var response: CinemaxResponse<List<MovieModel>>

    @Before
    fun setup() {
        movieRepository = mockk()
        useCase = GetMovieUseCase(movieRepository)
        mediaTypeModel = MediaTypeModel.Movie.Popular // Example MediaTypeModel
        movieModels = listOf(
            MovieModel(1, "Movie 1", "poster1.jpg"),
            MovieModel(2, "Movie 2", "poster2.jpg")
        )
        response = CinemaxResponse.Success(movieModels) // Example CinemaxResponse
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { movieRepository.getByMediaType(mediaTypeModel) } returns flowOf(CinemaxResponse.Loading)
        val result = useCase.invoke(mediaTypeModel).toList()
        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse = CinemaxResponse.Success(movieModels)
        every { movieRepository.getByMediaType(mediaTypeModel) } returns flowOf(successResponse)
        val result = useCase.invoke(mediaTypeModel).toList()
        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse = CinemaxResponse.Failure(errorMessage, null)
        every { movieRepository.getByMediaType(mediaTypeModel) } returns flowOf(errorResponse)
        val result = useCase.invoke(mediaTypeModel).toList()
        assertEquals(listOf(errorResponse), result)
    }
}