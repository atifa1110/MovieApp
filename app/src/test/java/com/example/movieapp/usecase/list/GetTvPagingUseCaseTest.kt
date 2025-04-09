package com.example.movieapp.usecase.list

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.repository.TvShowRepository
import com.example.movieapp.list.usecase.GetTvPagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTvPagingUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvPagingUseCase: GetTvPagingUseCase

    @Before
    fun setup() {
        tvShowRepository = mock()
        getTvPagingUseCase = GetTvPagingUseCase(tvShowRepository)
    }

    @Test
    fun `invoke - returns paging data flow`() = runTest {
        val mediaTypeModel = MediaTypeModel.TvShow.Popular
        val expectedPagingData: PagingData<TvShowModel> = PagingData.empty() // Or create a mock PagingData

        whenever(tvShowRepository.getPagingByMediaType(mediaTypeModel)).thenReturn(flowOf(expectedPagingData))

        val result = getTvPagingUseCase(mediaTypeModel).first()

        assertEquals(expectedPagingData, result)
    }

    @Test
    fun `invoke - correct media type passed to repository`() = runTest {
        val mediaTypeModel = MediaTypeModel.TvShow.NowPlaying
        val expectedPagingData: PagingData<TvShowModel> = PagingData.empty()

        whenever(tvShowRepository.getPagingByMediaType(mediaTypeModel)).thenReturn(flowOf(expectedPagingData))

        getTvPagingUseCase(mediaTypeModel)

        // No need to collect here, just verify the repository call
        verify(tvShowRepository).getPagingByMediaType(mediaTypeModel)
    }

    @Test
    fun `invoke - different media types`() = runTest {
        val mediaTypeModel1 = MediaTypeModel.TvShow.Popular
        val mediaTypeModel2 = MediaTypeModel.TvShow.TopRated
        val expectedPagingData1: PagingData<TvShowModel> = PagingData.empty()
        val expectedPagingData2: PagingData<TvShowModel> = PagingData.empty()

        whenever(tvShowRepository.getPagingByMediaType(mediaTypeModel1)).thenReturn(flowOf(expectedPagingData1))
        whenever(tvShowRepository.getPagingByMediaType(mediaTypeModel2)).thenReturn(flowOf(expectedPagingData2))

        getTvPagingUseCase(mediaTypeModel1)
        getTvPagingUseCase(mediaTypeModel2)

        verify(tvShowRepository).getPagingByMediaType(mediaTypeModel1)
        verify(tvShowRepository).getPagingByMediaType(mediaTypeModel2)
    }
}