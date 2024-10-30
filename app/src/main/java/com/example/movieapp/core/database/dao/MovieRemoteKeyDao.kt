package com.example.movieapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.util.Constants.Tables.MOVIES_REMOTE_KEYS
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity

@Dao
interface MovieRemoteKeyDao {
    @Query("SELECT * FROM $MOVIES_REMOTE_KEYS WHERE id = :id AND media_type = :mediaType")
    suspend fun getByIdAndMediaType(id: Int, mediaType: MediaType.Movie): MovieRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<MovieRemoteKeyEntity>)

    @Query("DELETE FROM $MOVIES_REMOTE_KEYS WHERE media_type = :mediaType")
    suspend fun deleteByMediaType(mediaType: MediaType.Movie)
}