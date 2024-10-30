package com.example.movieapp.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.core.database.converter.CreditsConverter
import com.example.movieapp.core.database.converter.ImagesConverter
import com.example.movieapp.core.database.converter.ListConverter
import com.example.movieapp.core.database.converter.VideosConverter
import com.example.movieapp.core.database.dao.GenreDao
import com.example.movieapp.core.database.dao.MovieDao
import com.example.movieapp.core.database.dao.MovieDetailsDao
import com.example.movieapp.core.database.dao.MovieRemoteKeyDao
import com.example.movieapp.core.database.dao.SearchDao
import com.example.movieapp.core.database.dao.WishlistDao
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.detailmovie.MovieDetailsEntity
import com.example.movieapp.core.database.model.movie.MovieEntity
import com.example.movieapp.core.database.model.movie.MovieGenreCrossRef
import com.example.movieapp.core.database.model.movie.MovieRemoteKeyEntity
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.database.model.wishlist.WishlistEntity

@Database(
    entities = [
        MovieEntity::class, MovieRemoteKeyEntity::class,
        MovieDetailsEntity::class, WishlistEntity::class, SearchEntity::class,
        GenreEntity::class, MovieGenreCrossRef::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(ListConverter::class, CreditsConverter::class, ImagesConverter::class, VideosConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao
    abstract fun movieDetailsDao(): MovieDetailsDao

    abstract fun wishListDao() : WishlistDao
    abstract fun searchHistoryDao() : SearchDao
    abstract fun genreDao() : GenreDao

    companion object {
        const val MOVIE_DATABASE = "movie.db"
    }
}