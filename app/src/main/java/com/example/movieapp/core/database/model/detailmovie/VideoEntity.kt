package com.example.movieapp.core.database.model.detailMovie

import com.example.movieapp.core.database.util.Constants
import com.google.gson.annotations.SerializedName

data class VideoEntity(
    @SerializedName(Constants.Fields.ID)
    val id: String,
    @SerializedName(Constants.Fields.ISO6391)
    val language: String,
    @SerializedName(Constants.Fields.ISO31661)
    val country: String,
    @SerializedName(Constants.Fields.KEY)
    val key: String,
    @SerializedName(Constants.Fields.NAME)
    val name: String,
    @SerializedName(Constants.Fields.SITE)
    val site: String,
    @SerializedName(Constants.Fields.SIZE)
    val size: Int,
    @SerializedName(Constants.Fields.TYPE)
    val type: String,
    @SerializedName(Constants.Fields.OFFICIAL)
    val official: Boolean,
    @SerializedName(Constants.Fields.PUBLISHED)
    val publishedAt: String
)