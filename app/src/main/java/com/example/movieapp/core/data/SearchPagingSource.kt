package com.example.movieapp.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.DEFAULT_PAGE
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val query: String,
    private val networkDataSource: MovieNetworkDataSource,
) : PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            val currentPage = params.key ?: DEFAULT_PAGE
            val response = networkDataSource.search(page = currentPage, query = query)

            when(response){
                is CinemaxResponse.Loading -> TODO()
                is CinemaxResponse.Success -> {
                    coroutineScope {
                        val data =
                            response.value.results?.filter { it.mediaType == "movie" || it.mediaType == "tv" }
                                ?.map { movieNetwork ->
                                    async {
                                        when(val runtime = networkDataSource.getDetailMovie(movieNetwork.id ?: 0)) {
                                            is CinemaxResponse.Success -> movieNetwork.asMovieModel(runtime.value.runtime)
                                            is CinemaxResponse.Failure -> movieNetwork.asMovieModel()
                                            CinemaxResponse.Loading -> null
                                        }
                                    }
                                }?.awaitAll()?.filterNotNull()

                        val endOfPaginationReached = data?.isEmpty() ?: false

                        val prevPage = if (currentPage == 1) null else currentPage - 1
                        val nextPage = if (endOfPaginationReached) null else currentPage + 1

                        LoadResult.Page(
                            data = data ?: emptyList(),
                            prevKey = prevPage,
                            nextKey = nextPage
                        )
                    }
                }

                is CinemaxResponse.Failure -> {
                    LoadResult.Error(Exception(response.error))
                }
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}

