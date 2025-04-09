package com.example.movieapp.usecase.login

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.usecase.SignInWithEmailAndPasswordUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInWithEmailAndPasswordUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: SignInWithEmailAndPasswordUseCase

    @Before
    fun setUp() {
        useCase = SignInWithEmailAndPasswordUseCase(authRepository)
    }
    @Test
    fun `invoke should return success response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val expectedToken = "test_token"
        val expectedResponse = CinemaxResponse.Success(expectedToken)

        every { authRepository.signInWithEmailAndPassword(email, password) } returns flowOf(
            expectedResponse
        )

        val result = useCase(email, password)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `invoke should return failure response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Authentication failed"
        val expectedResponse: CinemaxResponse<String> = CinemaxResponse.Failure(errorMessage)

        every { authRepository.signInWithEmailAndPassword(email, password) } returns flowOf(
            expectedResponse
        )

        val result = useCase(email, password)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `invoke should return loading response from authRepository`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val expectedResponse: CinemaxResponse<String> = CinemaxResponse.Loading

        every { authRepository.signInWithEmailAndPassword(email, password) } returns flowOf(
            expectedResponse
        )

        val result = useCase(email, password)

        result.collect { actualResponse ->
            assertEquals(expectedResponse, actualResponse)
        }
    }
}