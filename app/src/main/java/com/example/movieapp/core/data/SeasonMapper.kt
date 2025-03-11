package com.example.movieapp.core.data

import com.example.movieapp.core.database.mapper.asSeasonEntity
import com.example.movieapp.core.database.mapper.asSeasonModel
import com.example.movieapp.core.database.model.tv.SeasonEntity
import com.example.movieapp.core.network.response.tv.NetworkSeason

internal fun List<NetworkSeason>.asSeasonsEntity() = map(NetworkSeason::asSeasonEntity)

internal fun List<SeasonEntity>.asSeasonModels() = map(SeasonEntity::asSeasonModel)
