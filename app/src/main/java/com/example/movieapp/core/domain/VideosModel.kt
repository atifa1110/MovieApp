package com.example.movieapp.core.domain

import com.example.movieapp.core.network.Constants
import com.google.gson.annotations.SerializedName

data class VideosModel (
    val results : List<VideoModel>
)

data class VideoModel(
    val id: String,
    val language: String,
    val country: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String
)