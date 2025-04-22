package com.example.movieapp.core.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.model.detailMovie.MovieDetailsEntity
import com.example.movieapp.core.database.util.Constants.Tables.MOVIE_DETAILS
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailsDao {
    @Query("SELECT * FROM $MOVIE_DETAILS WHERE id = :id")
    fun getById(id: Int): Flow<MovieDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieDetails: MovieDetailsEntity)

    @Query("DELETE FROM $MOVIE_DETAILS WHERE id = :id")
    suspend fun deleteById(id: Int)
}