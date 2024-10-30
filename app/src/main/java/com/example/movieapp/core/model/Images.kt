package com.example.movieapp.core.model

data class Images(
    val backdrops: List<Image>,
    val posters : List<Image>
)

data class Image(
    val aspectRatio : Double,
    val height : Int,
    val width : Int,
    val filePath : String
)
