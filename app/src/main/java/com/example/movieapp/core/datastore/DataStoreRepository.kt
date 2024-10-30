package com.example.movieapp.core.datastore

import kotlinx.coroutines.flow.Flow


interface DataStoreRepository {
    suspend fun saveOnLoginState(completed : Boolean)
    suspend fun saveOnBoardingState(completed : Boolean)
    suspend fun saveOnAuthState(completed : Boolean)
    fun onLoginState() : Flow<Boolean>
    fun onBoardingState(): Flow<Boolean>
    fun onAuthState(): Flow<Boolean>
}