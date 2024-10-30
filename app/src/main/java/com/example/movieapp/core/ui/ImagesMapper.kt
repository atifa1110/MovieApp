package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.ImageModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.model.Image
import com.example.movieapp.core.model.Images

internal fun ImagesModel.asImages() = Images(
    backdrops = backdrops.map(ImageModel::asImage),
    posters = posters.map(ImageModel::asImage)
)

private fun ImageModel.asImage() = Image(
    aspectRatio = aspectRatio,
    height = height,
    width = width,
    filePath = filePath
)