package com.example.movieapp.usecase.profile

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.profile.usecase.LogoutUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = LogoutUseCase(authRepository)
    }

    @Test
    fun `invoke should call logout on authRepository`() {
        every { authRepository.logout() } returns Unit

        useCase.invoke()

        verify(exactly = 1) { authRepository.logout() }
    }
}