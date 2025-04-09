package com.example.movieapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.datastore.DataStorePreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class DataStorePreferenceTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataStorePreference: DataStorePreference

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        dataStore = PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("user_prefs") }
        dataStorePreference = DataStorePreference(dataStore)

    }

    @Test
    fun `saveOnBoardingState true and onBoardingState should work correctly`() = runBlocking {
        dataStorePreference.saveOnBoardingState(true)
        val result = dataStorePreference.onBoardingState().first()
        assertTrue(result)
    }

    @Test
    fun `saveOnBoardingState false and onBoardingState should work correctly`() = runBlocking {
        dataStorePreference.saveOnBoardingState(false)
        val result = dataStorePreference.onBoardingState().first()
        assertFalse(result)
    }

    @Test
    fun `saveOnLoginState true and onLoginState should work correctly`() = runBlocking {
        dataStorePreference.saveOnLoginState(true)
        val result = dataStorePreference.onLoginState().first()
        assertTrue(result)
    }

    @Test
    fun `saveOnLoginState false and onLoginState should work correctly`() = runBlocking {
        dataStorePreference.saveOnLoginState(false)
        val result = dataStorePreference.onLoginState().first()
        assertFalse(result)
    }

    @Test
    fun `saveOnAuthState true and onAuthState should work correctly`() = runBlocking {
        dataStorePreference.saveOnAuthState(true)
        val result = dataStorePreference.onAuthState().first()
        assertTrue(result)
    }

    @Test
    fun `saveOnAuthState false and onAuthState should work correctly`() = runBlocking {
        dataStorePreference.saveOnAuthState(false)
        val result = dataStorePreference.onAuthState().first()
        assertFalse(result)
    }

}