package com.example.movieapp.core.database.source

import androidx.paging.PagingSource
import com.example.movieapp.core.database.dao.tv.TvShowDao
import com.example.movieapp.core.database.dao.tv.TvShowRemoteKeyDao
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowDatabaseSource @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowRemoteKeyDao: TvShowRemoteKeyDao,
    private val transactionProvider: DatabaseTransactionProvider
) {
    fun getByMediaType(mediaType: MediaType.TvShow, pageSize: Int): Flow<List<TvShowEntity>> =
        tvShowDao.getByMediaType(mediaType, pageSize)

    fun getPagingByMediaType(mediaType: MediaType.TvShow): PagingSource<Int, TvShowEntity> =
        tvShowDao.getPagingByMediaType(mediaType)

    suspend fun insertAll(tvShows: List<TvShowEntity>) = tvShowDao.insertAll(tvShows)

    suspend fun deleteByMediaTypeAndInsertAll(
        mediaType: MediaType.TvShow,
        tvShows: List<TvShowEntity>
    ) = transactionProvider.runWithTransaction {
        tvShowDao.deleteByMediaType(mediaType)
        tvShowDao.insertAll(tvShows)
    }

    suspend fun getRemoteKeyByIdAndMediaType(id: Int, mediaType: MediaType.TvShow) =
        tvShowRemoteKeyDao.getByIdAndMediaType(id, mediaType)

    suspend fun handlePaging(
        mediaType: MediaType.TvShow,
        movies: List<TvShowEntity>,
        remoteKeys: List<TvShowRemoteKeyEntity>,
        shouldDeleteMoviesAndRemoteKeys: Boolean
    ) = transactionProvider.runWithTransaction {
        if (shouldDeleteMoviesAndRemoteKeys) {
            tvShowDao.deleteByMediaType(mediaType)
            tvShowRemoteKeyDao.deleteByMediaType(mediaType)
        }
        tvShowRemoteKeyDao.insertAll(remoteKeys)
        tvShowDao.insertAll(movies)
    }

}