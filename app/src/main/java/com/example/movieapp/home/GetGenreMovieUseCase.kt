package com.example.movieapp.home

import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenreMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke() : Flow<CinemaxResponse<List<GenreModel>>> {
        return movieRepository.genreMovie()
    }
}