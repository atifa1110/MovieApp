package com.example.movieapp.core.database.model.detailmovie

data class CreditsEntity(
    val cast : List<CastEntity>,
    val crew : List<CrewEntity>
)