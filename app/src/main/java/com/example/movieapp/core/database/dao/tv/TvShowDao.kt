package com.example.movieapp.core.database.dao.tv

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.model.tv.TvShowEntity
import com.example.movieapp.core.database.util.Constants
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {
    @Query("SELECT * FROM ${Constants.Tables.TV_SHOWS} WHERE media_type = :mediaType LIMIT :pageSize")
    fun getByMediaType(mediaType: MediaType.TvShow, pageSize: Int): Flow<List<TvShowEntity>>

    @Query("SELECT * FROM ${Constants.Tables.TV_SHOWS} WHERE media_type = :mediaType")
    fun getPagingByMediaType(mediaType: MediaType.TvShow): PagingSource<Int, TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tvShows: List<TvShowEntity>)

    @Query("DELETE FROM ${Constants.Tables.TV_SHOWS} WHERE media_type = :mediaType")
    suspend fun deleteByMediaType(mediaType: MediaType.TvShow)
}