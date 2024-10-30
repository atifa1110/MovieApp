package com.example.movieapp.list

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviePagingUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(mediaTypeModel: MediaTypeModel.Movie) : Flow<PagingData<MovieModel>> {
        return movieRepository.getPagingByMediaType(mediaTypeModel)
    }
}