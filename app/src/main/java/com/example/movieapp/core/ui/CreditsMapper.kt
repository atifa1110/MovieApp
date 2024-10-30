package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.CastModel
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.CrewModel
import com.example.movieapp.core.model.Cast
import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.Crew

internal fun CreditsModel.asCredits() = Credits(
    cast = cast.map(CastModel::asCast),
    crew = crew.map(CrewModel::asCrew)
)

private fun CastModel.asCast() = Cast(
    id = id,
    name = name,
    character = character,
    profilePath = profilePath
)

private fun CrewModel.asCrew() = Crew(
    id = id,
    name = name,
    job = job,
    profilePath = profilePath
)