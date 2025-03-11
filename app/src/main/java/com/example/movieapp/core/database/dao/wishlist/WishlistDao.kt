package com.example.movieapp.core.database.dao.wishlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.util.Constants.Tables.WISHLIST
import com.example.movieapp.core.database.util.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM $WISHLIST")
    fun getByMediaType(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishlist: WishlistEntity)

    @Query("DELETE FROM $WISHLIST WHERE media_type = :mediaType AND network_id = :id")
    suspend fun deleteByMediaTypeAndNetworkId(mediaType: MediaType.Wishlist, id: Int)

    @Query("SELECT EXISTS(SELECT * FROM $WISHLIST WHERE media_type = :mediaType AND network_id = :id)")
    suspend fun isWishListed(mediaType: MediaType.Wishlist, id: Int): Boolean
}