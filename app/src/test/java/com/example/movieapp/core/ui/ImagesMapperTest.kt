package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.ImageModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.model.Images
import org.junit.Assert.assertEquals
import org.junit.Test

class ImagesMapperTest {

    @Test
    fun `test ImagesModel asImages() maps correctly`() {
        // Given: A sample ImagesModel with ImageModels
        val imageModel1 = ImageModel(aspectRatio = 1.5, height = 1080, width = 720, filePath = "/path1.jpg")
        val imageModel2 = ImageModel(aspectRatio = 1.8, height = 1920, width = 1080, filePath = "/path2.jpg")

        val imagesModel = ImagesModel(
            backdrops = listOf(imageModel1),
            posters = listOf(imageModel2)
        )

        // When: Mapping occurs
        val result: Images = imagesModel.asImages()

        // Then: Assert that the mapping is correct
        assertEquals(1, result.backdrops.size)
        assertEquals(1, result.posters.size)

        assertEquals(imageModel1.aspectRatio, result.backdrops[0].aspectRatio, 0.0)
        assertEquals(imageModel1.height, result.backdrops[0].height)
        assertEquals(imageModel1.width, result.backdrops[0].width)
        assertEquals(imageModel1.filePath, result.backdrops[0].filePath)

        assertEquals(imageModel2.aspectRatio, result.posters[0].aspectRatio, 0.0)
        assertEquals(imageModel2.height, result.posters[0].height)
        assertEquals(imageModel2.width, result.posters[0].width)
        assertEquals(imageModel2.filePath, result.posters[0].filePath)
    }
}
