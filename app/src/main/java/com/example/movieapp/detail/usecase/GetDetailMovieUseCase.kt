package com.example.movieapp.detail.usecase

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieDetailRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailMovieUseCase @Inject constructor(
    private val detailRepository : MovieDetailRepository
) {
    operator fun invoke(id : Int): Flow<CinemaxResponse<MovieDetailModel?>> {
        return detailRepository.getDetailMovie(id)
    }
}