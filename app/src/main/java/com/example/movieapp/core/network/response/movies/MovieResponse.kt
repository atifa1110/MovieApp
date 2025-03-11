package com.example.movieapp.core.network.response.movies

import com.example.movieapp.core.network.Constants
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName(Constants.Fields.PAGE)
    val page: Int,
    @SerializedName(Constants.Fields.RESULTS)
    val results: List<MovieNetwork>?= emptyList(),
    @SerializedName(Constants.Fields.TOTAL_PAGES)
    val totalPages: Int?=0,
    @SerializedName(Constants.Fields.TOTAL_RESULTS)
    val totalResults: Int?=0
)