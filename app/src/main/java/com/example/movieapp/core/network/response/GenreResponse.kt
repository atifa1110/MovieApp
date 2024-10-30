package com.example.movieapp.core.network.response

import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.GenreNetwork
import com.google.gson.annotations.SerializedName

data class GenreResponse (
    @SerializedName(Constants.Fields.GENRES)
    val genres: List<GenreNetwork>?= emptyList(),
)