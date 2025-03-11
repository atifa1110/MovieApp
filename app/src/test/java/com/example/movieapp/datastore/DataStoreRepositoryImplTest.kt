package com.example.movieapp.datastore

import com.example.movieapp.core.datastore.DataStorePreference
import com.example.movieapp.core.datastore.DataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DataStoreRepositoryImplTest {

    // Mock DataStorePreference
    private lateinit var dataStorePreference: DataStorePreference

    // Class under test
    private lateinit var dataStoreRepository: DataStoreRepositoryImpl

    // Test dispatcher for coroutine testing
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Initialize mock
        dataStorePreference = mock(DataStorePreference::class.java)

        // Initialize class under test
        dataStoreRepository = DataStoreRepositoryImpl(dataStorePreference)
    }

    @Test
    fun `saveOnLoginState should call saveOnLoginState in dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val completed = true

        // Act
        dataStoreRepository.saveOnLoginState(completed)

        // Assert
        verify(dataStorePreference).saveOnLoginState(completed)
    }

    @Test
    fun `saveOnBoardingState should call saveOnBoardingState in dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val completed = true

        // Act
        dataStoreRepository.saveOnBoardingState(completed)

        // Assert
        verify(dataStorePreference).saveOnBoardingState(completed)
    }

    @Test
    fun `saveOnAuthState should call saveOnAuthState in dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val completed = true

        // Act
        dataStoreRepository.saveOnAuthState(completed)

        // Assert
        verify(dataStorePreference).saveOnAuthState(completed)
    }

    @Test
    fun `onLoginState should return flow from dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val expectedFlow = flowOf(true)
        whenever(dataStorePreference.onLoginState()).thenReturn(expectedFlow)

        // Act
        val result = dataStoreRepository.onLoginState().first()

        // Assert
        assertEquals(true, result)
        verify(dataStorePreference).onLoginState()
    }

    @Test
    fun `onBoardingState should return flow from dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val expectedFlow = flowOf(false)
        whenever(dataStorePreference.onBoardingState()).thenReturn(expectedFlow)

        // Act
        val result = dataStoreRepository.onBoardingState().first()

        // Assert
        assertEquals(false, result)
        verify(dataStorePreference).onBoardingState()
    }

    @Test
    fun `onAuthState should return flow from dataStorePreference`() = runTest(testDispatcher) {
        // Arrange
        val expectedFlow = flowOf(true)
        whenever(dataStorePreference.onAuthState()).thenReturn(expectedFlow)

        // Act
        val result = dataStoreRepository.onAuthState().first()

        // Assert
        assertEquals(true, result)
        verify(dataStorePreference).onAuthState()
    }
}
