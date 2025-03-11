package com.example.movieapp.core.database.dao.tv

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.model.tv.TvShowRemoteKeyEntity
import com.example.movieapp.core.database.util.Constants
import com.example.movieapp.core.database.util.MediaType

@Dao
interface TvShowRemoteKeyDao {
    @Query("SELECT * FROM ${Constants.Tables.TV_SHOWS_REMOTE_KEYS} WHERE id = :id AND media_type = :mediaType")
    suspend fun getByIdAndMediaType(id: Int, mediaType: MediaType.TvShow): TvShowRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<TvShowRemoteKeyEntity>)

    @Query("DELETE FROM ${Constants.Tables.TV_SHOWS_REMOTE_KEYS} WHERE media_type = :mediaType")
    suspend fun deleteByMediaType(mediaType: MediaType.TvShow)
}