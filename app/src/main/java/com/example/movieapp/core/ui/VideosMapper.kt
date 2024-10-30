package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.VideoModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.Video
import com.example.movieapp.core.model.Videos

fun VideosModel.asVideos() = Videos(
    results = results.map(VideoModel::asVideos)
)
private fun VideoModel.asVideos() = Video(
    id = id,
    country = country,
    language = language,
    key = key,
    name = name,
    official = official,
    publishedAt = publishedAt,
    site = site,
    size = size,
    type = type
)