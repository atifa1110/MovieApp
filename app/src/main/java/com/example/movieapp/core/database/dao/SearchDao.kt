package com.example.movieapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.database.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM ${Constants.Tables.SEARCH} ORDER BY timestamp DESC")
    fun getSearchHistory(): Flow<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchEntity : SearchEntity)

    @Query("DELETE FROM ${Constants.Tables.SEARCH} WHERE id = :id")
    suspend fun deleteSearchHistory(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM ${Constants.Tables.SEARCH} WHERE id = :id)")
    suspend fun isSearchExist(id: Int): Boolean
}