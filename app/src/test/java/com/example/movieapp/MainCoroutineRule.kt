package com.example.movieapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)  // Sets Dispatchers.Main to our test dispatcher
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()  // Resets Dispatchers.Main back to its original state
        testDispatcher.cleanupTestCoroutines()  // Cleans up the test dispatcher
    }
}