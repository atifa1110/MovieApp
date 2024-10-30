package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.VideoEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.domain.VideoModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.network.response.NetworkVideo
import com.example.movieapp.core.network.response.NetworkVideos

fun NetworkVideos.asVideoEntity() = VideosEntity(
    results = results.map(NetworkVideo::asVideoEntity)
)

private fun NetworkVideo.asVideoEntity() = VideoEntity(
    id = id,
    language = language,
    country = country,
    key = key,
    name = name,
    official = official,
    publishedAt = publishedAt,
    site = site,
    size = size,
    type = type
)

fun VideosEntity.asVideoModel() = VideosModel(
    results = results.map(VideoEntity::asVideoModel)
)

private fun VideoEntity.asVideoModel() = VideoModel(
    id = id,
    language = language,
    country = country,
    key = key,
    name = name,
    official = official,
    publishedAt = publishedAt,
    site = site,
    size = size,
    type = type
)