package com.example.movieapp.usecase.profile

import android.net.Uri
import com.example.movieapp.core.network.repository.StorageRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.profile.usecase.UploadImageUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class UploadImageUseCaseTest {

    private lateinit var storageRepository: StorageRepository
    private lateinit var useCase: UploadImageUseCase
    private lateinit var imageUri: Uri
    private val uploadedImageUrl = "uploaded_image_url"

    @Before
    fun setup() {
        storageRepository = mockk()
        useCase = UploadImageUseCase(storageRepository)
        imageUri = mockk() // Mock Uri
    }
    @Test
    fun `invoke should return success response when upload image is successful`() = runBlocking {
        // Arrange
        val storageRepository: StorageRepository = mockk()
        val uploadImageUseCase = UploadImageUseCase(storageRepository)
        val imageUri: Uri = mockk()
        val expectedResult = CinemaxResponse.Success("https://example.com/image.jpg")

        every { storageRepository.uploadImageUri(imageUri) } returns flowOf(expectedResult)

        // Act
        val result = uploadImageUseCase(imageUri).toList()

        // Assert
        assertEquals(listOf(expectedResult), result)
    }

    @Test
    fun `invoke should return failure response when upload image fails`() = runBlocking {
        // Arrange
        val storageRepository: StorageRepository = mockk()
        val uploadImageUseCase = UploadImageUseCase(storageRepository)
        val imageUri: Uri = mockk()
        val errorMessage = "Image upload failed"
        val expectedResult = CinemaxResponse.Failure(errorMessage,null)

        every { storageRepository.uploadImageUri(imageUri) } returns flowOf(expectedResult)

        // Act
        val result = uploadImageUseCase(imageUri).toList()

        // Assert
        assertEquals(listOf(expectedResult), result)
    }

    @Test
    fun `invoke should return loading then success response`() = runBlocking {
        // Arrange
        val storageRepository: StorageRepository = mockk()
        val uploadImageUseCase = UploadImageUseCase(storageRepository)
        val imageUri: Uri = mockk()
        val expectedResult = listOf(
            CinemaxResponse.Loading,
            CinemaxResponse.Success("https://example.com/image.jpg")
        )

        every { storageRepository.uploadImageUri(imageUri) } returns flowOf(CinemaxResponse.Loading, CinemaxResponse.Success("https://example.com/image.jpg"))

        // Act
        val result = uploadImageUseCase(imageUri).toList()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return loading then failure response`() = runBlocking {
        // Arrange
        val storageRepository: StorageRepository = mockk()
        val uploadImageUseCase = UploadImageUseCase(storageRepository)
        val imageUri: Uri = mockk()
        val errorMessage = "Image upload failed"
        val expectedResult = listOf(
            CinemaxResponse.Loading,
            CinemaxResponse.Failure(errorMessage)
        )

        every { storageRepository.uploadImageUri(imageUri) } returns flowOf(CinemaxResponse.Loading, CinemaxResponse.Failure(errorMessage))

        // Act
        val result = uploadImageUseCase(imageUri).toList()

        // Assert
        assertEquals(expectedResult, result)
    }
}