package com.example.movieapp.core.network.repository

import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<CinemaxResponse<List<SearchModel>>>
    suspend fun addMovieToSearchHistory(searchModel: SearchModel)
    suspend fun removeMovieFromSearchHistory(id: Int)
    suspend fun isSearchExist(id: Int) : Boolean
}