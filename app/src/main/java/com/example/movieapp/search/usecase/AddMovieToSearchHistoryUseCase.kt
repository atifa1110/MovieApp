package com.example.movieapp.search.usecase

import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.repository.SearchHistoryRepository
import javax.inject.Inject

class AddMovieToSearchHistoryUseCase @Inject constructor(
    private val searchRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(searchModel: SearchModel) {
        searchRepository.addMovieToSearchHistory(searchModel)
    }
}