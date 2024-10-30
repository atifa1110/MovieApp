package com.example.movieapp.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.movieapp.core.database.util.Constants.Tables.MOVIES
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieGenreCrossRef
import com.example.movieapp.core.database.model.movie.MovieWithGenreNames
import com.example.movieapp.core.database.model.movie.MovieWithGenres
import com.example.movieapp.core.database.util.Constants
import com.example.movieapp.core.database.util.Constants.Tables.GENRES
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM $MOVIES WHERE media_type = :mediaType LIMIT :pageSize")
    fun getByMediaType(mediaType: MediaType.Movie, pageSize: Int): Flow<List<MovieEntity>>

    @Query("SELECT * FROM $MOVIES WHERE media_type = :mediaType")
    fun getPagingByMediaType(mediaType: MediaType.Movie): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(movies: List<MovieGenreCrossRef>)

    @Query("DELETE FROM $MOVIES WHERE media_type = :mediaType")
    suspend fun deleteByMediaType(mediaType: MediaType.Movie)

    // Fetch movie data with genre names using JOIN
    //@Transaction
    //@Query("SELECT * FROM $MOVIES")
    //fun getByMediaTypeByGenre(mediaType: MediaType.Movie, pageSize: Int) : Flow<List<MovieWithGenreNames>>

}