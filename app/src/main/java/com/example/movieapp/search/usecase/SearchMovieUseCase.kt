package com.example.movieapp.search.usecase

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val searchRepository: MovieRepository
) {
    operator fun invoke(query: String): Flow<PagingData<MovieModel>> {
        return searchRepository.searchMovie(query)
    }
}