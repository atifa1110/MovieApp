package com.example.movieapp.usecase.home

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.repository.TvShowRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.home.usecase.GetTvShowUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetTvShowUseCaseTest{
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var useCase: GetTvShowUseCase
    private lateinit var mediaTypeModel: MediaTypeModel.TvShow
    private lateinit var tvShowModels: List<TvShowModel>

    @Before
    fun setup() {
        tvShowRepository = mockk()
        useCase = GetTvShowUseCase(tvShowRepository)
        mediaTypeModel = MediaTypeModel.TvShow.Popular // Example MediaTypeModel
        tvShowModels = listOf(
            TvShowModel(1, "Tv Show 1","",0.0,"", emptyList(),"","", emptyList(),0.0,0,"","",0.0),
            TvShowModel(2, "Tv Show 2","",0.0,"", emptyList(),"","", emptyList(),0.0,0,"","",0.0)
        )
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { tvShowRepository.getByMediaType(mediaTypeModel) } returns flowOf(CinemaxResponse.Loading)

        val result = useCase.invoke(mediaTypeModel).toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<List<TvShowModel>> = CinemaxResponse.Success(tvShowModels)
        every { tvShowRepository.getByMediaType(mediaTypeModel) } returns flowOf(successResponse)

        val result = useCase.invoke(mediaTypeModel).toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Repository error"
        val errorResponse: CinemaxResponse<List<TvShowModel>> = CinemaxResponse.Failure(errorMessage)
        every { tvShowRepository.getByMediaType(mediaTypeModel) } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<List<TvShowModel>>> = useCase.invoke(mediaTypeModel).toList()

        assertEquals(listOf(errorResponse), result)
    }
}