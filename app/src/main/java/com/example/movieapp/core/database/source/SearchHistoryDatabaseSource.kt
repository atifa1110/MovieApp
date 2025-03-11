package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.search.SearchDao
import com.example.movieapp.core.database.model.search.SearchEntity
import javax.inject.Inject

class SearchHistoryDatabaseSource @Inject constructor(
    private val searchDao: SearchDao
) {
    fun getSearchHistory() = searchDao.getSearchHistory()
    suspend fun addMovieToSearchHistory(searchEntity: SearchEntity) = searchDao.insertSearchHistory(searchEntity)
    suspend fun deleteMovieToSearchHistory(id: Int) = searchDao.deleteSearchHistory(id)
    suspend fun isSearchExist(id: Int) = searchDao.isSearchExist(id)
}
