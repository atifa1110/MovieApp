package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.movie.Genre
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.GenreNetworkWithId

internal fun GenreNetwork.asGenre() = id.asGenre()
internal fun List<GenreNetwork>.asGenres() = map(GenreNetwork::asGenre)

@JvmName("intListAsGenres")
internal fun List<Int>.asGenres() = map(Int::asGenre)

@JvmName("intListAsGenreModels")
internal fun List<Int>.asGenreModels() = map(Int::asGenreModel)

internal fun Genre.asGenreModel() = GenreModel[genreName]
internal fun List<Genre>.asGenreModels() = map(Genre::asGenreModel)

private fun String.asGenreModel() = GenreModel[this]
private fun Int.asGenre() = Genre[GenreNetworkWithId[this].genreName]
private fun Int.asGenreModel() = GenreModel[GenreNetworkWithId[this].genreName]

internal fun List<GenreEntity>.asGenreModel() = map(GenreEntity::asGenreModel)
internal fun GenreEntity.asGenreModel() = name.asGenreModel()

internal fun List<GenreModel>.asGenreEntity() = map(GenreModel::asGenreEntity)
internal fun GenreModel.asGenreEntity() = GenreEntity(
    name = genreName
)

