package com.example.movieapp.usecase.profile

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.example.movieapp.profile.usecase.GetProfileUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetProfileUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: GetProfileUseCase
    private lateinit var user: User

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = GetProfileUseCase(authRepository)
        user = User("Test Name", "test@email.com", "test_profile_url") // Example User object
    }

    @Test
    fun `invoke should return loading response`() = runBlocking {
        every { authRepository.getProfile() } returns flowOf(CinemaxResponse.Loading)

        val result = useCase.invoke().toList()

        assertEquals(listOf(CinemaxResponse.Loading), result)
    }

    @Test
    fun `invoke should return success response`() = runBlocking {
        val successResponse: CinemaxResponse<User> = CinemaxResponse.Success(user)
        every { authRepository.getProfile() } returns flowOf(successResponse)

        val result: List<CinemaxResponse<User>> = useCase.invoke().toList()

        assertEquals(listOf(successResponse), result)
    }

    @Test
    fun `invoke should return failure response`() = runBlocking {
        val errorMessage = "Authentication error"
        val errorResponse: CinemaxResponse<User> = CinemaxResponse.Failure(errorMessage)
        every { authRepository.getProfile() } returns flowOf(errorResponse)

        val result: List<CinemaxResponse<User>> = useCase.invoke().toList()

        assertEquals(listOf(errorResponse), result)
    }
}