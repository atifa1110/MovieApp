package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailMovie.ImageEntity
import com.example.movieapp.core.database.model.detailMovie.ImagesEntity
import com.example.movieapp.core.domain.ImageModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.response.movies.NetworkImage
import com.example.movieapp.core.network.response.movies.NetworkImages


fun NetworkImages.asImagesEntity() = ImagesEntity(
    backdrops = backdrops.map(NetworkImage::asImageEntity),
    posters = posters.map(NetworkImage::asImageEntity)
)

internal fun NetworkImage.asImageEntity() = ImageEntity(
    aspectRatio = aspectRatio,
    height = height,
    width = width,
    filePath = filePath.asImageURL(),
)

fun ImagesEntity.asImagesModel() = ImagesModel(
    backdrops = backdrops.map(ImageEntity::asImageModel),
    posters = posters.map(ImageEntity::asImageModel)
)

internal fun ImageEntity.asImageModel() = ImageModel(
    aspectRatio = aspectRatio,
    height = height,
    width = width,
    filePath = filePath,
)