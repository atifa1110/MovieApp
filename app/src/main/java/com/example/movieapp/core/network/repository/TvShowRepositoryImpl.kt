package com.example.movieapp.core.network.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.movieapp.core.data.TvShowRemoteMediator
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.asMovieEntity
import com.example.movieapp.core.data.asMovieModel
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.data.asTvShowEntity
import com.example.movieapp.core.data.asTvShowModel
import com.example.movieapp.core.data.listMap
import com.example.movieapp.core.data.pagingMap
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.PAGE_SIZE
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.defaultPagingConfig
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val databaseDataSource: TvShowDatabaseSource,
    private val networkDataSource: TvShowNetworkDataSource
) : TvShowRepository {

    override fun getByMediaType(mediaTypeModel: MediaTypeModel.TvShow): Flow<CinemaxResponse<List<TvShowModel>>> {
        val mediaType = mediaTypeModel.asMediaType()
        return networkBoundResource(
            query = {  databaseDataSource.getByMediaType(
                mediaType = mediaType, pageSize = PAGE_SIZE
            ).listMap(TvShowEntity::asTvShowModel)
            },
            fetch = { networkDataSource.getByMediaType(mediaType.asNetworkMediaType()) },
            saveFetchResult = { response ->
                val result = response.results
                databaseDataSource.deleteByMediaTypeAndInsertAll(
                    mediaType = mediaType,
                    tvShows = result.map { it.asTvShowEntity(mediaType) }
                )
            },
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingByMediaType(mediaTypeModel: MediaTypeModel.TvShow): Flow<PagingData<TvShowModel>> {
        val mediaType = mediaTypeModel.asMediaType()
        return Pager(
            config = defaultPagingConfig,
            remoteMediator = TvShowRemoteMediator(databaseDataSource, networkDataSource, mediaType),
            pagingSourceFactory = { databaseDataSource.getPagingByMediaType(mediaType) }
        ).flow.pagingMap(TvShowEntity::asTvShowModel)
    }

}