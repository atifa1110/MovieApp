package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.tv.TvShowDetailsDao
import com.example.movieapp.core.database.model.tv.TvShowDetailsEntity
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import javax.inject.Inject

class TvShowDetailsDatabaseSource @Inject constructor(
    private val tvShowDetailsDao: TvShowDetailsDao,
    private val transactionProvider: DatabaseTransactionProvider
) {
    fun getById(id: Int) = tvShowDetailsDao.getById(id)
    suspend fun deleteAndInsert(tvShowDetails: TvShowDetailsEntity) =
        transactionProvider.runWithTransaction {
            tvShowDetailsDao.deleteById(id = tvShowDetails.id)
            tvShowDetailsDao.insert(tvShowDetails)
        }
}