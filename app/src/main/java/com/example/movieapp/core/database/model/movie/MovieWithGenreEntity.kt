package com.example.movieapp.core.database.model.movie

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class MovieWithGenres (
    val movie: MovieEntity,
    val genreNames: List<String>
)
