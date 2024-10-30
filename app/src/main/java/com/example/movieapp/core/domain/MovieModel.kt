package com.example.movieapp.core.domain

data class MovieModel(
    val id: Int? = 0,
    val title: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val releaseDate: String? = "",
    val adult: Boolean? = false,
    val genres: List<GenreModel>? = emptyList(),
    val originalTitle: String? = "",
    val originalLanguage: String? = "",
    val voteAverage: Double? = 0.0,
    val voteCount: Int? = 0,
    val posterPath: String? = "",
    val profilePath: String? = "",
    val backdropPath: String? = "",
    val video: Boolean? = false,
    val rating : Double? = 0.0,
    val mediaType : String? = "",
    val runtime : Int? = 0,
)