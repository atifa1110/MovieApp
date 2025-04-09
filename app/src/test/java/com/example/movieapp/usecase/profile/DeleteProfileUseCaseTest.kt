package com.example.movieapp.usecase.profile

import com.example.movieapp.core.network.repository.StorageRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.profile.usecase.DeleteProfileUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DeleteProfileUseCaseTest {

    private lateinit var storageRepository: StorageRepository
    private lateinit var useCase: DeleteProfileUseCase
    private val imageUrl = "test_image_url"

    @Before
    fun setup() {
        storageRepository = mockk()
        useCase = DeleteProfileUseCase(storageRepository)
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { storageRepository.deleteProfileFromStorage(imageUrl) } returns flowOf(CinemaxResponse.Loading)

        val result: List<CinemaxResponse<String>> = useCase.invoke(imageUrl).toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<String> = CinemaxResponse.Success("Delete successful")
        every { storageRepository.deleteProfileFromStorage(imageUrl) } returns flowOf(successResponse)

        val result: List<CinemaxResponse<String>> = useCase.invoke(imageUrl).toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Storage error"
        val errorResponse: CinemaxResponse<String> = CinemaxResponse.Failure(errorMessage)
        every { storageRepository.deleteProfileFromStorage(imageUrl) } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<String>> = useCase.invoke(imageUrl).toList()

        assertEquals(listOf(errorResponse), result)
    }
}