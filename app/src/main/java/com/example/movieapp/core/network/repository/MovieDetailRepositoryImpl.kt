package com.example.movieapp.core.network.repository

import com.example.movieapp.core.data.asMovieDetailsEntity
import com.example.movieapp.core.data.asMovieDetailsModel
import com.example.movieapp.core.database.source.MovieDetailsDatabaseSource
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val databaseDataSource: MovieDetailsDatabaseSource,
    private val networkDataSource: MovieNetworkDataSource,
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : MovieDetailRepository {

    override fun getDetailMovie(id: Int): Flow<CinemaxResponse<MovieDetailModel?>> {
       return networkBoundResource(
           query = { databaseDataSource.getById(id).map{ it?.asMovieDetailsModel(isWishListed = wishlistDatabaseSource.isMovieWishListed(id)) } },
           fetch = { networkDataSource.getDetailMovie(id) },
           saveFetchResult = { response ->
                databaseDataSource.deleteAndInsert(response.asMovieDetailsEntity())
           }
       )
    }
}