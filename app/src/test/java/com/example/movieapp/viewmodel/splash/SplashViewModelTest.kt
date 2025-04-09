package com.example.movieapp.viewmodel.splash

import com.example.movieapp.core.datastore.DataStoreRepository
import com.example.movieapp.splash.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class SplashViewModelTest {

    private val dataStoreRepository: DataStoreRepository = mockk(relaxed = true)
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = SplashViewModel(dataStoreRepository)
    }

    @Test
    fun `onBoardingState should return flow from DataStoreRepository`() = runTest {
        val expectedState = true
        every { dataStoreRepository.onBoardingState() } returns flowOf(expectedState)

        viewModel = SplashViewModel(dataStoreRepository)

        viewModel.onBoardingState.collect { actualState ->
            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `onLoginState should return flow from DataStoreRepository`() = runTest {
        val expectedState = true
        every { dataStoreRepository.onLoginState() } returns flowOf(expectedState)

        viewModel = SplashViewModel(dataStoreRepository)

        viewModel.onLoginState.collect { actualState ->
            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `onAuthState should return flow from DataStoreRepository`() = runTest {
        val expectedState = true
        every { dataStoreRepository.onAuthState() } returns flowOf(expectedState)

        viewModel = SplashViewModel(dataStoreRepository)

        viewModel.onAuthState.collect { actualState ->
            assertEquals(expectedState, actualState)
        }
    }
}