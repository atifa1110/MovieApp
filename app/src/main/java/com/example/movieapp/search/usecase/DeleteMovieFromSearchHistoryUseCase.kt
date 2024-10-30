package com.example.movieapp.search.usecase

import com.example.movieapp.core.network.repository.SearchHistoryRepository
import javax.inject.Inject

class DeleteMovieFromSearchHistoryUseCase @Inject constructor(
    private val searchRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(id: Int) {
        searchRepository.removeMovieFromSearchHistory(id)
    }
}