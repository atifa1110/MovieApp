package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.MovieDetailsDao
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import javax.inject.Inject

class MovieDetailsDatabaseSource @Inject constructor(
    private val movieDetailsDao: MovieDetailsDao,
    private val transactionProvider: DatabaseTransactionProvider
) {
    fun getById(id: Int) = movieDetailsDao.getById(id)
    fun getByIds(ids: List<Int>) = movieDetailsDao.getByIds(ids)

    suspend fun deleteAndInsert(movieDetails: MovieDetailsEntity) =
        transactionProvider.runWithTransaction {
            movieDetailsDao.deleteById(id = movieDetails.id)
            movieDetailsDao.insert(movieDetails)
        }

    suspend fun deleteAndInsertAll(movieDetailsList: List<MovieDetailsEntity>) =
        transactionProvider.runWithTransaction {
            movieDetailsList.forEach { movieDetails ->
                movieDetailsDao.deleteById(id = movieDetails.id)
                movieDetailsDao.insert(movieDetails)
            }
        }
}