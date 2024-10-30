package com.example.movieapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.datastore.DataStorePreference
import com.example.movieapp.home.GetUserId
import com.example.movieapp.home.HomeUiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userId : FirebaseUser? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserId: GetUserId,
    private val logoutUseCase: LogoutUseCase,
    private val dataStorePreference: DataStorePreference
) : ViewModel(){

        private val _uiState = MutableStateFlow(ProfileUiState(userId = getUserId()))
        val uiState = _uiState.asStateFlow()

        fun logout(){
            logoutUseCase.invoke()
            setOnLoginState(false)
        }

        private fun setOnLoginState(completed : Boolean){
            viewModelScope.launch {
                dataStorePreference.saveOnLoginState(completed)
            }
        }
}