package com.example.movieapp.core.database.model.detailMovie

data class CreditsEntity(
    val cast : List<CastEntity>,
    val crew : List<CrewEntity>
)