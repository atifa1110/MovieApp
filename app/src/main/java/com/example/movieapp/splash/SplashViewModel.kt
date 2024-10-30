package com.example.movieapp.splash

import androidx.lifecycle.ViewModel
import com.example.movieapp.core.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel(){

    val onBoardingState = dataStoreRepository.onBoardingState()
    val onLoginState = dataStoreRepository.onLoginState()
    val onAuthState = dataStoreRepository.onAuthState()
}