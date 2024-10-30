package com.example.movieapp.core.domain

data class ImagesModel(
    val backdrops : List<ImageModel>,
    val posters : List<ImageModel>
)

data class ImageModel (
    val aspectRatio : Double,
    val height : Int,
    val width : Int,
    val filePath : String,
)