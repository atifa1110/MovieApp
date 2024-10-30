package com.example.movieapp.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.core.datastore.DataStoreRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.usecase.SignInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email : String = "",
    val emailError : String? = null,
    val password : String = "",
    val passwordError : String? = null,
    val isLoading : Boolean = false,
    val isLogin : Boolean = false,
    val userMessage : String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$"
        val error = when {
            newEmail.isBlank() -> "Email cannot be empty"
            !newEmail.matches(emailRegex.toRegex()) -> "Invalid email format"
            else -> null
        }
        _uiState.update {
            it.copy(email = newEmail, emailError = error)
        }
    }

    fun onPasswordChange(newPassword: String) {
        val error = when {
            newPassword.isBlank() -> "Password cannot be empty"
            newPassword.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
        _uiState.update {
            it.copy(password = newPassword, passwordError = error)
        }
    }

    private fun setLoginState(completed: Boolean){
        viewModelScope.launch {
            dataStoreRepository.saveOnLoginState(completed)
        }
    }

    fun snackBarMessageShown(){
        _uiState.value = _uiState.value.copy(userMessage = null)
    }

    fun signInEmailAndPassword() = viewModelScope.launch {
        signInWithEmailAndPasswordUseCase.invoke(uiState.value.email, uiState.value.password)
            .collect { result ->
                when (result) {
                    is CinemaxResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLogin = true,
                                userMessage = result.value
                            )
                        }
                        setLoginState(true)
                    }

                    is CinemaxResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is CinemaxResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = result.error
                            )
                        }
                    }
                }
            }
    }
}