package com.example.movieapp.search.usecase

import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieWithGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(mediaTypeModel: MediaTypeModel.Movie): Flow<CinemaxResponse<List<MovieModel>>> {
        return movieRepository.getByMediaTypeGenre(mediaTypeModel)
    }
}