package com.example.movieapp.core.datastore

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreference: DataStorePreference
) : DataStoreRepository{
    override suspend fun saveOnLoginState(completed: Boolean) {
        dataStorePreference.saveOnLoginState(completed)
    }

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStorePreference.saveOnBoardingState(completed)
    }

    override suspend fun saveOnAuthState(completed: Boolean) {
        dataStorePreference.saveOnAuthState(completed)
    }

    override fun onLoginState(): Flow<Boolean> {
        return dataStorePreference.onLoginState()
    }

    override fun onBoardingState(): Flow<Boolean> {
        return dataStorePreference.onBoardingState()
    }

    override fun onAuthState(): Flow<Boolean> {
        return dataStorePreference.onAuthState()
    }

}