package com.example.movieapp.core.model

data class MovieDetails (
    val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<Genre>,
    val posterPath: String?,
    val releaseDate: String?,
    val runtime: Int,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val credits: Credits?,
    val rating : Double,
    val images : Images?,
    val videos : Videos?,
    val isWishListed: Boolean
)