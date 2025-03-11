package com.example.movieapp.core.data


import com.example.movieapp.core.database.model.detailmovie.CastEntity
import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.CrewEntity
import com.example.movieapp.core.domain.CastModel
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.CrewModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.response.movies.NetworkCast
import com.example.movieapp.core.network.response.movies.NetworkCredits
import com.example.movieapp.core.network.response.movies.NetworkCrew

fun NetworkCredits.asCredits() = CreditsEntity(
    cast = cast.map(NetworkCast::asCast),
    crew = crew.map(NetworkCrew::asCrew)
)

fun NetworkCast.asCast() = CastEntity(
    id = id,
    name = name,
    adult = adult,
    castId = castId,
    character = character,
    creditId = creditId,
    gender = gender,
    knownForDepartment = knownForDepartment,
    order = order,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath?.asImageURL()
)

fun NetworkCrew.asCrew() = CrewEntity(
    id = id,
    adult = adult,
    creditId = creditId,
    department = department,
    gender = gender,
    job = job,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath?.asImageURL()
)

fun CreditsEntity.asCreditsModel() = CreditsModel(
    cast = cast.map(CastEntity::asCastModel),
    crew = crew.map(CrewEntity::asCrewModel)
)

fun CastEntity.asCastModel() = CastModel(
    id = id,
    name = name,
    adult = adult,
    castId = castId,
    character = character,
    creditId = creditId,
    gender = gender,
    knownForDepartment = knownForDepartment,
    order = order,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath
)

fun CrewEntity.asCrewModel() = CrewModel(
    id = id,
    adult = adult,
    creditId = creditId,
    department = department,
    gender = gender,
    job = job,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath
)