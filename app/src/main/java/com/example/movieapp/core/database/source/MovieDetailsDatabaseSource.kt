package com.example.movieapp.core.database.source

import com.example.movieapp.core.database.dao.movie.MovieDetailsDao
import com.example.movieapp.core.database.model.detailMovie.MovieDetailsEntity
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import javax.inject.Inject

class MovieDetailsDatabaseSource @Inject constructor(
    private val movieDetailsDao: MovieDetailsDao,
    private val transactionProvider: DatabaseTransactionProvider
) {
    fun getById(id: Int) = movieDetailsDao.getById(id)
    suspend fun deleteAndInsert(movieDetails: MovieDetailsEntity) =
        transactionProvider.runWithTransaction {
            movieDetailsDao.deleteById(id = movieDetails.id)
            movieDetailsDao.insert(movieDetails)
        }
}