package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.ImageEntity
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.response.movies.NetworkImage
import com.example.movieapp.core.network.response.movies.NetworkImages
import org.junit.Assert.assertEquals
import org.junit.Test

class ImagesMapperTest {

    @Test
    fun `test NetworkImage to ImageEntity mapping`() {
        val networkImage = NetworkImage(
            aspectRatio = 1.78,
            height = 1080,
            width = 1920,
            filePath = "/image.jpg"
        )

        val imageEntity = networkImage.asImageEntity()

        assertEquals(networkImage.aspectRatio, imageEntity.aspectRatio, 0.0)
        assertEquals(networkImage.height, imageEntity.height)
        assertEquals(networkImage.width, imageEntity.width)
        assertEquals(networkImage.filePath.asImageURL(), imageEntity.filePath)
    }

    @Test
    fun `test NetworkImages to ImagesEntity mapping`() {
        val expected = "/backdrop.jpg"
        val expected1 = "/poster.jpg"
        val networkImages = NetworkImages(
            backdrops = listOf(
                NetworkImage(1.78, 1080, 1920, "/backdrop.jpg")
            ),
            posters = listOf(
                NetworkImage(2.0, 1500, 1000, "/poster.jpg")
            )
        )

        val imagesEntity = networkImages.asImagesEntity()

        assertEquals(1, imagesEntity.backdrops.size)
        assertEquals(expected.asImageURL(), imagesEntity.backdrops[0].filePath)

        assertEquals(1, imagesEntity.posters.size)
        assertEquals(expected1.asImageURL(), imagesEntity.posters[0].filePath)
    }

    @Test
    fun `test ImageEntity to ImageModel mapping`() {
        val imageEntity = ImageEntity(
            aspectRatio = 1.78,
            height = 1080,
            width = 1920,
            filePath = "/image.jpg"
        )

        val imageModel = imageEntity.asImageModel()

        assertEquals(imageEntity.aspectRatio, imageModel.aspectRatio, 0.0)
        assertEquals(imageEntity.height, imageModel.height)
        assertEquals(imageEntity.width, imageModel.width)
        assertEquals(imageEntity.filePath, imageModel.filePath)
    }

    @Test
    fun `test ImagesEntity to ImagesModel mapping`() {
        val imagesEntity = ImagesEntity(
            backdrops = listOf(
                ImageEntity(1.78, 1080, 1920, "/backdrop.jpg")
            ),
            posters = listOf(
                ImageEntity(2.0, 1500, 1000, "/poster.jpg")
            )
        )

        val imagesModel = imagesEntity.asImagesModel()

        assertEquals(1, imagesModel.backdrops.size)
        assertEquals("/backdrop.jpg", imagesModel.backdrops[0].filePath)

        assertEquals(1, imagesModel.posters.size)
        assertEquals("/poster.jpg", imagesModel.posters[0].filePath)
    }
}