package com.example.movieapp.usecase.register

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.example.movieapp.register.usecase.RegisterWithEmailAndPasswordUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RegisterWithEmailAndPasswordUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: RegisterWithEmailAndPasswordUseCase

    @Before
    fun setUp() {
        useCase = RegisterWithEmailAndPasswordUseCase(authRepository)
    }

    @Test
    fun `invoke should return success response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = User("Test User")
        val expectedToken = "test_token"
        val expectedResponse = CinemaxResponse.Success(expectedToken)

        every { authRepository.registerWithEmailAndPassword(email, password, user) } returns flowOf(expectedResponse)

        val result = useCase(email, password, user)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `invoke should return failure response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = User("Test User")
        val errorMessage = "Registration failed"
        val expectedResponse: CinemaxResponse<String> = CinemaxResponse.Failure(errorMessage)

        every { authRepository.registerWithEmailAndPassword(email, password, user) } returns flowOf(expectedResponse)

        val result = useCase(email, password, user)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `invoke should return loading response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = User("Test User")
        val expectedResponse: CinemaxResponse<String> = CinemaxResponse.Loading

        every { authRepository.registerWithEmailAndPassword(email, password, user) } returns flowOf(expectedResponse)

        val result = useCase(email, password, user)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }
}