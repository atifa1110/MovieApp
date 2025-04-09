package com.example.movieapp.viewmodel.onboarding

import com.example.movieapp.core.datastore.DataStoreRepository
import com.example.movieapp.onboarding.OnBoardingViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class OnBoardingViewModelTest {

    private val dataStoreRepository: DataStoreRepository = mockk(relaxed = true)
    private lateinit var viewModel: OnBoardingViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = OnBoardingViewModel(dataStoreRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setOnBoardingState should call saveOnBoardingState on DataStoreRepository with correct boolean`() = runTest {
        val completedState = true

        viewModel.setOnBoardingState(completedState)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { dataStoreRepository.saveOnBoardingState(completedState) }
    }

    @Test
    fun `setOnBoardingState should call saveOnBoardingState on DataStoreRepository with false boolean`() = runTest {
        val completedState = false

        viewModel.setOnBoardingState(completedState)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { dataStoreRepository.saveOnBoardingState(completedState) }
    }
}