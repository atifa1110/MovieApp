package com.example.movieapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.source.TvShowDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.database.util.PagingUtils
import com.example.movieapp.core.network.datasource.TvShowNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class TvShowRemoteMediator(
    private val databaseDataSource: TvShowDatabaseSource,
    private val networkDataSource: TvShowNetworkDataSource,
    private val mediaType: MediaType.TvShow
) : RemoteMediator<Int, TvShowEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TvShowEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKey?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKey?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    nextPage
                }
            }

            val response = networkDataSource.getByMediaType(
                mediaType = mediaType.asNetworkMediaType(),
                page = currentPage
            )
            when(response) {
                is CinemaxResponse.Success -> {
                    coroutineScope {
                        val tv = response.value.results.map { it.asTvShowEntity(mediaType, 0) }
                        val endOfPaginationReached = tv.isEmpty()

                        val prevPage = if (currentPage == 1) null else currentPage - 1
                        val nextPage = if (endOfPaginationReached) null else currentPage + 1

                        val remoteKeys = tv.map { entity ->
                            TvShowRemoteKeyEntity(
                                id = entity.id,
                                mediaType = mediaType,
                                prevPage = prevPage,
                                nextPage = nextPage
                            )
                        }

                        databaseDataSource.handlePaging(
                            mediaType = mediaType,
                            movies = tv ,
                            remoteKeys = remoteKeys,
                            shouldDeleteMoviesAndRemoteKeys = loadType == LoadType.REFRESH
                        )

                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    }
                }
                is CinemaxResponse.Failure -> {
                    MediatorResult.Error(Exception(response.error))
                }

                CinemaxResponse.Loading -> TODO()
            }

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, TvShowEntity>
    ): TvShowRemoteKeyEntity? = PagingUtils.getRemoteKeyClosestToCurrentPosition(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, TvShowEntity>
    ): TvShowRemoteKeyEntity? = PagingUtils.getRemoteKeyForFirstItem(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, TvShowEntity>
    ): TvShowRemoteKeyEntity? = PagingUtils.getRemoteKeyForLastItem(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }
}