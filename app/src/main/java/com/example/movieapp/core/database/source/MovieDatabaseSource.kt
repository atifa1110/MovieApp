package com.example.movieapp.core.database.source

import android.util.Log
import androidx.paging.PagingSource
import com.example.movieapp.core.database.dao.genre.GenreDao
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.database.dao.movie.MovieDao
import com.example.movieapp.core.database.dao.movie.MovieRemoteKeyDao
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieGenreCrossRef
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieDatabaseSource @Inject constructor(
    private val movieDao: MovieDao,
    private val movieRemoteKeyDao: MovieRemoteKeyDao,
    private val genreDao: GenreDao,
    private val transactionProvider: DatabaseTransactionProvider
) {
    fun getGenreMovie() : Flow<List<GenreEntity>> = genreDao.getGenres()

    suspend fun insertGenre(
        genres : List<GenreEntity>
    ) = transactionProvider.runWithTransaction {
        genreDao.deleteAll()
        genreDao.insertAll(genres)
    }

    //Fetch movies with genre names
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getMoviesWithGenres(mediaType: MediaType.Movie, pageSize: Int): Flow<List<MovieWithGenres>> {
        return movieDao.getByMediaType(mediaType,pageSize).flatMapLatest { movies ->
            Log.d("Debug","Movie Map : ${movies.size}")
            val moviesWithGenresFlow = movies.map { movie ->
                // Fetch genres based on genre IDs and map to names
                val genreFlow = movie.genreIds?.let { ids ->
                    genreDao.getGenresByIds(ids).map { genres ->
                        genres.map { it.name }
                    }
                } ?: flowOf(emptyList()) // Handle null or empty genreIds
                Log.d("Debug","Genre Map : ${genreFlow.first().size}")
                // Combine the movie with its genre names
                genreFlow.map { genreNames ->
                    MovieWithGenres(movie, genreNames)
                }
            }
            Log.d("Debug","Movies with Flow Map : ${moviesWithGenresFlow.size}")
            // Combine all individual movie flows into a single flow
            combine(moviesWithGenresFlow) { it.toList() }
        }
    }

    fun getByMediaType(mediaType: MediaType.Movie, pageSize: Int): Flow<List<MovieEntity>> =
        movieDao.getByMediaType(mediaType, pageSize)

    fun getPagingByMediaType(mediaType: MediaType.Movie): PagingSource<Int, MovieEntity> =
        movieDao.getPagingByMediaType(mediaType)

    suspend fun deleteByMediaTypeAndInsertAll(
        mediaType: MediaType.Movie,
        movies: List<MovieEntity>
    ) = transactionProvider.runWithTransaction {
        movieDao.deleteByMediaType(mediaType)
        movieDao.insertAll(movies)
    }

    suspend fun getRemoteKeyByIdAndMediaType(id: Int, mediaType: MediaType.Movie) =
        movieRemoteKeyDao.getByIdAndMediaType(id, mediaType)

    suspend fun handlePaging(
        mediaType: MediaType.Movie,
        movies: List<MovieEntity>,
        remoteKeys: List<MovieRemoteKeyEntity>,
        shouldDeleteMoviesAndRemoteKeys: Boolean
    ) = transactionProvider.runWithTransaction {
        if (shouldDeleteMoviesAndRemoteKeys) {
            movieDao.deleteByMediaType(mediaType)
            movieRemoteKeyDao.deleteByMediaType(mediaType)
        }
        movieRemoteKeyDao.insertAll(remoteKeys)
        movieDao.insertAll(movies)
    }

}