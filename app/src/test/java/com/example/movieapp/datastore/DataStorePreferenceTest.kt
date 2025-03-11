package com.example.movieapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.core.datastore.DataStorePreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import java.io.File
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
    fun saveAndGetOnBoardingState() = runBlocking {
        dataStorePreference.saveOnBoardingState(true)
        val result = dataStorePreference.onBoardingState().first()
        assertTrue(result)
    }

    @Test
    fun saveAndGetLoginState() = runBlocking {
        dataStorePreference.saveOnLoginState(true)
        val result = dataStorePreference.onLoginState().first()
        assertTrue(result)
    }

    @Test
    fun saveAndGetAuthState() = runBlocking {
        dataStorePreference.saveOnAuthState(true)
        val result = dataStorePreference.onAuthState().first()
        assertTrue(result)
    }

    @Test
    fun clearDataClearsAllStates() = runBlocking {
        dataStorePreference.saveOnBoardingState(true)
        dataStorePreference.saveOnLoginState(true)
        dataStorePreference.saveOnAuthState(true)

        dataStorePreference.clearData()

        val onboardingState = dataStorePreference.onBoardingState().first()
        val loginState = dataStorePreference.onLoginState().first()
        val authState = dataStorePreference.onAuthState().first()

        assertFalse(onboardingState)
        assertFalse(loginState)
        assertFalse(authState)
    }
}