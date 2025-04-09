package com.example.movieapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.database.util.PagingUtils.getRemoteKeyClosestToCurrentPosition
import com.example.movieapp.core.database.util.PagingUtils.getRemoteKeyForFirstItem
import com.example.movieapp.core.database.util.PagingUtils.getRemoteKeyForLastItem
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val databaseDataSource: MovieDatabaseSource,
    private val networkDataSource: MovieNetworkDataSource,
    private val mediaType: MediaType.Movie
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
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
                        val movies = response.value.results?.map { movieNetwork ->
                                async {
                                    when (val runtime = networkDataSource.getDetailMovie(movieNetwork.id ?: 0)) {
                                        is CinemaxResponse.Success -> movieNetwork.asMovieEntity(mediaType,runtime.value.runtime)
                                        is CinemaxResponse.Failure -> null
                                        CinemaxResponse.Loading -> null
                                    }
                                }
                            }?.awaitAll()?.filterNotNull()


                        val endOfPaginationReached = movies?.isEmpty() ?: false

                        val prevPage = if (currentPage == 1) null else currentPage - 1
                        val nextPage = if (endOfPaginationReached) null else currentPage + 1

                        val remoteKeys = movies?.map { entity ->
                            MovieRemoteKeyEntity(
                                id = entity.id,
                                mediaType = mediaType,
                                prevPage = prevPage,
                                nextPage = nextPage
                            )
                        }

                        databaseDataSource.handlePaging(
                            mediaType = mediaType,
                            movies = movies ?: emptyList(),
                            remoteKeys = remoteKeys ?: emptyList(),
                            shouldDeleteMoviesAndRemoteKeys = loadType == LoadType.REFRESH
                        )

                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    }
                }
                is CinemaxResponse.Failure -> {
                    MediatorResult.Error(Exception(response.error))
                }

                is CinemaxResponse.Loading -> {
                    MediatorResult.Success(endOfPaginationReached = false)
                }
                // 🔥 Tambahkan else untuk menangani semua kasus
                else -> MediatorResult.Error(Exception("Unhandled response type: $response"))
            }

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? = getRemoteKeyClosestToCurrentPosition(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? = getRemoteKeyForFirstItem(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? = getRemoteKeyForLastItem(state) { entity ->
        databaseDataSource.getRemoteKeyByIdAndMediaType(
            id = entity.networkId,
            mediaType = mediaType
        )
    }

}