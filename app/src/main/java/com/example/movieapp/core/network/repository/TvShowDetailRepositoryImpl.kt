package com.example.movieapp.core.network.repository

import com.example.movieapp.core.data.asMovieDetailsEntity
import com.example.movieapp.core.data.asMovieDetailsModel
import com.example.movieapp.core.data.asTvShowDetailsEntity
import com.example.movieapp.core.data.asTvShowDetailsModel
import com.example.movieapp.core.database.source.TvShowDetailsDatabaseSource
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TvShowDetailRepositoryImpl @Inject constructor(
    private val databaseDataSource: TvShowDetailsDatabaseSource,
    private val networkDataSource: TvShowNetworkDataSource,
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : TvShowDetailRepository {
    override fun getDetailTvShow(id: Int): Flow<CinemaxResponse<TvShowDetailModel?>> {
        return networkBoundResource(
            query = { databaseDataSource.getById(id).map{ it?.asTvShowDetailsModel(isWishListed = wishlistDatabaseSource.isTvShowWishListed(id)) } },
            fetch = { networkDataSource.getDetailTv(id) },
            saveFetchResult = { response ->
                databaseDataSource.deleteAndInsert(response.asTvShowDetailsEntity())
            }
        )
    }

}
