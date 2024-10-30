package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.network.GenreNetwork


fun GenreNetwork.asGenreEntity() = GenreEntity(
    id = id,
    name = name
)

fun GenreEntity.asGenreModel() = GenreModel(
    id = id,
    name = name
)