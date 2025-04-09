package com.example.movieapp.usecase.home

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.home.usecase.GetUserId
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetUserIdTest{

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: GetUserId
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = GetUserId(authRepository)
        firebaseUser = mockk() // Mock FirebaseUser
    }

    @Test
    fun `invoke should return FirebaseUser from repository`() {
        every { authRepository.getUserId() } returns firebaseUser

        val result: FirebaseUser? = useCase.invoke()

        assertEquals(firebaseUser, result)
    }

    @Test
    fun `invoke should return null if repository returns null`() {
        every { authRepository.getUserId() } returns null

        val result: FirebaseUser? = useCase.invoke()

        assertEquals(null, result)
    }
}