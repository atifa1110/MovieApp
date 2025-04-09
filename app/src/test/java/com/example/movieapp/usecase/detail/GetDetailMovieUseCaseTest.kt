package com.example.movieapp.usecase.detail

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.network.repository.MovieDetailRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.detail.usecase.GetDetailMovieUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetDetailMovieUseCaseTest {
    private lateinit var detailRepository: MovieDetailRepository
    private lateinit var useCase: GetDetailMovieUseCase
    private val movieId = 123
    private lateinit var movieDetailModel: MovieDetailModel

    @Before
    fun setup() {
        detailRepository = mockk()
        useCase = GetDetailMovieUseCase(detailRepository)
        movieDetailModel = MovieDetailModel(
            1, false,"",0,
            emptyList(),"","","","title","overview",0.0,"","",0,0,"","","title",false,0.0,0,0.0,
            CreditsModel(emptyList(), emptyList()), ImagesModel(emptyList(), emptyList()),
            VideosModel(emptyList()),false)
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { detailRepository.getDetailMovie(movieId) } returns flowOf(CinemaxResponse.Loading)

        val result: List<CinemaxResponse<MovieDetailModel?>> = useCase.invoke(movieId).toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<MovieDetailModel?> = CinemaxResponse.Success(movieDetailModel)
        every { detailRepository.getDetailMovie(movieId) } returns flowOf(successResponse)

        val result: List<CinemaxResponse<MovieDetailModel?>> = useCase.invoke(movieId).toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<MovieDetailModel?> = CinemaxResponse.Failure(errorMessage)
        every { detailRepository.getDetailMovie(movieId) } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<MovieDetailModel?>> = useCase.invoke(movieId).toList()

        assertEquals(listOf(errorResponse), result)
    }

    @Test
    fun `invoke should return success response with null MovieDetailModel`() = runBlocking {
        val successResponse: CinemaxResponse<MovieDetailModel?> = CinemaxResponse.Success(null)
        every { detailRepository.getDetailMovie(movieId) } returns flowOf(successResponse)

        val result: List<CinemaxResponse<MovieDetailModel?>> = useCase.invoke(movieId).toList()

        assertEquals(listOf(successResponse), result)
    }
}