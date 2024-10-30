package com.example.movieapp.core.network.repository

import com.example.movieapp.core.data.listMap
import com.example.movieapp.core.database.mapper.asSearchEntity
import com.example.movieapp.core.database.mapper.asSearchModel
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.database.source.SearchHistoryDatabaseSource
import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchDatabaseSource: SearchHistoryDatabaseSource
) : SearchHistoryRepository{

    override fun getSearchHistory(): Flow<CinemaxResponse<List<SearchModel>>> {
        return flow {
            try{
                emit(CinemaxResponse.Loading)
                val result = searchDatabaseSource.getSearchHistory().listMap(SearchEntity::asSearchModel).first()
                emit(CinemaxResponse.Success(result))
            } catch (e: Exception) {
                emit(CinemaxResponse.Failure(e.localizedMessage ?: "Unexpected Error!"))
            }
        }
    }

    override suspend fun addMovieToSearchHistory(searchModel: SearchModel) {
        return searchDatabaseSource.addMovieToSearchHistory(searchModel.asSearchEntity())
    }

    override suspend fun removeMovieFromSearchHistory(id: Int) {
        return searchDatabaseSource.deleteMovieToSearchHistory(id)
    }

    override suspend fun isSearchExist(id: Int): Boolean {
        return searchDatabaseSource.isSearchExist(id)
    }
}