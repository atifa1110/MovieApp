package com.example.movieapp.core.database.model.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.movieapp.core.database.util.Constants

@Entity(
    primaryKeys = ["movieId", "genreId"],
)

data class MovieGenreCrossRef(
    @ColumnInfo(name = Constants.Fields.MOVIE_ID)
    val movieId: Int,

    @ColumnInfo(name = Constants.Fields.GENRE_ID)
    val genreId: Int
)