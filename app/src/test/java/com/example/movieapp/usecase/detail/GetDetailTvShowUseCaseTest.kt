package com.example.movieapp.usecase.detail

import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.repository.TvShowDetailRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.detail.usecase.GetDetailTvShowUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetDetailTvShowUseCaseTest {

    private lateinit var detailRepository: TvShowDetailRepository
    private lateinit var useCase: GetDetailTvShowUseCase
    private val tvShowId = 123
    private lateinit var tvShowDetailModel: TvShowDetailModel

    @Before
    fun setup() {
        detailRepository = mockk()
        useCase = GetDetailTvShowUseCase(detailRepository)
        tvShowDetailModel = TvShowDetailModel(1, "Title", false,"", emptyList(),"",
            emptyList(), emptyList(),"",false, emptyList(),"",0, 0, emptyList(),"","","overview",0.0,
            "","","","",0.0, 0, CreditsModel(emptyList(), emptyList()),
            0.0,  false)
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { detailRepository.getDetailTvShow(tvShowId) } returns flowOf(CinemaxResponse.Loading)

        val result: List<CinemaxResponse<TvShowDetailModel?>> = useCase.invoke(tvShowId).toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<TvShowDetailModel?> = CinemaxResponse.Success(tvShowDetailModel)
        every { detailRepository.getDetailTvShow(tvShowId) } returns flowOf(successResponse)

        val result: List<CinemaxResponse<TvShowDetailModel?>> = useCase.invoke(tvShowId).toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<TvShowDetailModel?> = CinemaxResponse.Failure(errorMessage)
        every { detailRepository.getDetailTvShow(tvShowId) } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<TvShowDetailModel?>> = useCase.invoke(tvShowId).toList()

        assertEquals(listOf(errorResponse), result)
    }

    @Test
    fun `invoke should return success response with null TvShowDetailModel`() = runBlocking {
        val successResponse: CinemaxResponse<TvShowDetailModel?> = CinemaxResponse.Success(null)
        every { detailRepository.getDetailTvShow(tvShowId) } returns flowOf(successResponse)

        val result: List<CinemaxResponse<TvShowDetailModel?>> = useCase.invoke(tvShowId).toList()

        assertEquals(listOf(successResponse), result)
    }
}