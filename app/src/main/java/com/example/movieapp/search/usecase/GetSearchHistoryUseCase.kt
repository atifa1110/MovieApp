package com.example.movieapp.search.usecase

import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.repository.SearchHistoryRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val searchRepository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<CinemaxResponse<List<SearchModel>>> {
        return searchRepository.getSearchHistory()
    }

}