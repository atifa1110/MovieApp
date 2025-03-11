package com.example.movieapp.core.network.response.tv

import com.example.movieapp.core.network.Constants
import com.google.gson.annotations.SerializedName

data class TvShowResponse(
    @SerializedName(Constants.Fields.PAGE)
    val page: Int,

    @SerializedName(Constants.Fields.RESULTS)
    val results: List<TvShowNetwork>,

    @SerializedName(Constants.Fields.TOTAL_PAGES)
    val totalPages: Int,

    @SerializedName(Constants.Fields.TOTAL_RESULTS)
    val totalResults: Int,
)