package com.example.movieapp.core.model

data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val firstAirDate: String?,
    val genres: List<Genre>,
    val voteAverage: Double,
    val posterPath: String?,
    val backdropPath: String?,
    val rating : Double?
)
