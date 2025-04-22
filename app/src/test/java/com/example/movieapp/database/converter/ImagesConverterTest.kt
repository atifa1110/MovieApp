package com.example.movieapp.database.converter

import com.example.movieapp.core.database.converter.ImagesConverter
import com.example.movieapp.core.database.model.detailMovie.ImageEntity
import com.example.movieapp.core.database.model.detailMovie.ImagesEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ImagesConverterTest {

    private val imagesConverter = ImagesConverter()
    private val gson = Gson()

    @Test
    fun `fromImages - converts ImagesEntity to JSON`() {
        val images = ImagesEntity(
            posters = listOf(
                ImageEntity(filePath = "/poster1.jpg", height = 0, width = 0, aspectRatio = 1.5),
                ImageEntity(filePath = "/poster1.jpg", height = 0, width = 0, aspectRatio = 1.5),
            ),
            backdrops = emptyList() // You can add backdrops if needed for your test
        )

        val json = imagesConverter.fromImages(images)

        assertNotNull(json)
        assertEquals(gson.toJson(images), json)
    }

    @Test
    fun `toImages - converts JSON to ImagesEntity`() {
        val images = ImagesEntity(
            posters = listOf(
                ImageEntity(filePath = "/poster1.jpg", height = 0, width = 0, aspectRatio = 1.5),
                ImageEntity(filePath = "/poster1.jpg", height = 0, width = 0, aspectRatio = 1.5),
            ),
            backdrops = emptyList() // You can add backdrops if needed for your test
        )

        val json = gson.toJson(images)

        val convertedImages = imagesConverter.toImages(json)

        assertNotNull(convertedImages)
        assertEquals(images, convertedImages)
    }

    // Add more test cases as needed (e.g., handling null, empty lists, etc.)
}