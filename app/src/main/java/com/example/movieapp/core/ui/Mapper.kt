package com.example.movieapp.core.ui

import com.example.movieapp.core.database.mapper.Genre
import com.example.movieapp.core.database.mapper.GenreModel

fun List<GenreModel>.asGenreNames() : List<String> {
    return this.map { it.name }
}
