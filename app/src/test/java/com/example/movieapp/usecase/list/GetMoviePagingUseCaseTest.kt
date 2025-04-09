package com.example.movieapp.usecase.list

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.list.usecase.GetMoviePagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetMoviePagingUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviePagingUseCase: GetMoviePagingUseCase

    @Before
    fun setup() {
        movieRepository = mock()
        getMoviePagingUseCase = GetMoviePagingUseCase(movieRepository)
    }

    @Test
    fun `invoke - returns paging data flow`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.Popular
        val expectedPagingData: PagingData<MovieModel> = PagingData.empty() // Or create a mock PagingData

        whenever(movieRepository.getPagingByMediaType(mediaTypeModel)).thenReturn(flowOf(expectedPagingData))

        val result = getMoviePagingUseCase(mediaTypeModel).first()

        // Collect the first (and only) emitted value from the Flow

        assertEquals(expectedPagingData, result)
    }

    @Test
    fun `invoke - correct media type passed to repository`() = runTest {
        val mediaTypeModel = MediaTypeModel.Movie.NowPlaying
        val expectedPagingData: PagingData<MovieModel> = PagingData.empty()

        whenever(movieRepository.getPagingByMediaType(mediaTypeModel)).thenReturn(flowOf(expectedPagingData))

        getMoviePagingUseCase(mediaTypeModel)

        // No need to collect here, just verify the repository call
        verify(movieRepository).getPagingByMediaType(mediaTypeModel)
    }

    @Test
    fun `invoke - different media types`() = runTest {
        val mediaTypeModel1 = MediaTypeModel.Movie.Popular
        val mediaTypeModel2 = MediaTypeModel.Movie.Upcoming
        val expectedPagingData1: PagingData<MovieModel> = PagingData.empty()
        val expectedPagingData2: PagingData<MovieModel> = PagingData.empty()

        whenever(movieRepository.getPagingByMediaType(mediaTypeModel1)).thenReturn(flowOf(expectedPagingData1))
        whenever(movieRepository.getPagingByMediaType(mediaTypeModel2)).thenReturn(flowOf(expectedPagingData2))

        getMoviePagingUseCase(mediaTypeModel1)
        getMoviePagingUseCase(mediaTypeModel2)

        verify(movieRepository).getPagingByMediaType(mediaTypeModel1)
        verify(movieRepository).getPagingByMediaType(mediaTypeModel2)
    }
}