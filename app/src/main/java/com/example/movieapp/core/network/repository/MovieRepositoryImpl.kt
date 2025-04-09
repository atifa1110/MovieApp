package com.example.movieapp.core.network.repository

import android.util.Log

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.movieapp.core.data.asMediaType
import com.example.movieapp.core.data.MovieRemoteMediator
import com.example.movieapp.core.data.asMovieModel
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.source.MovieDatabaseSource
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.data.SearchPagingSource
import com.example.movieapp.core.data.asMovieEntity
import com.example.movieapp.core.data.asNetworkMediaType
import com.example.movieapp.core.data.listMap
import com.example.movieapp.core.database.mapper.asGenreEntity
import com.example.movieapp.core.database.mapper.asGenreModel
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.PAGE_SIZE
import com.example.movieapp.core.network.datasource.MovieNetworkDataSource
import com.example.movieapp.core.network.defaultPagingConfig
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.pagingMap
import com.example.movieapp.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val databaseDataSource: MovieDatabaseSource,
    private val networkDataSource: MovieNetworkDataSource
) : MovieRepository {

    override fun genreMovie(): Flow<CinemaxResponse<List<GenreModel>>> {
        return networkBoundResource(
            query = { databaseDataSource.getGenreMovie().listMap(GenreEntity::asGenreModel) },
            fetch = { networkDataSource.getGenreMovie() },
            saveFetchResult = { response ->
                val result = response.genres?.map(GenreNetwork::asGenreEntity)?: emptyList()
                databaseDataSource.insertGenre(result)
            }
        )
    }

    override fun searchMovie(query: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                SearchPagingSource(
                    query = query,
                    networkDataSource = networkDataSource,
                )
            }
        ).flow
    }

    override fun getByMediaTypeGenre(mediaTypeModel: MediaTypeModel.Movie): Flow<CinemaxResponse<List<MovieModel>>> {
        val mediaType = mediaTypeModel.asMediaType()
        return networkBoundResource(
            query = {  databaseDataSource.getMoviesWithGenres(
                mediaType = mediaType, pageSize = PAGE_SIZE).listMap(MovieWithGenres::asMovieModel)
            },
            fetch = { networkDataSource.getByMediaType(mediaType.asNetworkMediaType()) },
            saveFetchResult = { response ->
                val result = response.results ?: emptyList()
                Log.d("Result","Result Response : ${result.size}")
                databaseDataSource.deleteByMediaTypeAndInsertAll(
                    mediaType = mediaType,
                    movies = result.map { it.asMovieEntity(mediaType) }
                )
            }
        )
    }

    override fun getByMediaType(mediaTypeModel: MediaTypeModel.Movie): Flow<CinemaxResponse<List<MovieModel>>> {
        val mediaType = mediaTypeModel.asMediaType()
        return networkBoundResource(
            query = {  databaseDataSource.getByMediaType(
                mediaType = mediaType, pageSize = PAGE_SIZE).listMap(MovieEntity::asMovieModel)
            },
            fetch = { networkDataSource.getByMediaType(mediaType.asNetworkMediaType()) },
            saveFetchResult = { response ->
                val result = response.results?: emptyList()
                databaseDataSource.deleteByMediaTypeAndInsertAll(
                    mediaType = mediaType,
                    movies = result.map { it.asMovieEntity(mediaType) }
                )
            },
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingByMediaType(mediaTypeModel: MediaTypeModel.Movie): Flow<PagingData<MovieModel>> {
        val mediaType = mediaTypeModel.asMediaType()
        return Pager(
            config = defaultPagingConfig,
            remoteMediator = MovieRemoteMediator(databaseDataSource, networkDataSource, mediaType),
            pagingSourceFactory = { databaseDataSource.getPagingByMediaType(mediaType) }
        ).flow.pagingMap(MovieEntity::asMovieModel)
    }

}
