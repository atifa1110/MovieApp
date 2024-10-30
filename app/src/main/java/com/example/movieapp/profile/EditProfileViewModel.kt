package com.example.movieapp.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val isSheetOpen : Boolean = false,
    val userName : String? = "",
    val name : String? = "",
    val nameError : String? = null,
    val email : String? = "",
    val phoneNumber : String? = "",
    val phoneError : String? = null,
    val photo : String? = "",
    val isLoading : Boolean = false,
    val userMessage : String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProfile()
    }

    fun onSheetOpen(open: Boolean) {
        _uiState.update { it.copy(isSheetOpen = open)}
    }

    fun snackBarMessageShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }

    fun onNameChange(newName : String){
        val error = when {
            newName.isBlank() -> "Name cannot be empty"
            else -> null
        }
        _uiState.update { it.copy(name = newName,nameError = error) }
    }

    fun onPhoneNumberChange(newPhone : String){
        val error = when {
            newPhone.isBlank() -> "Phone Number cannot be empty"
            else -> null
        }
        _uiState.update { it.copy(phoneNumber = newPhone, phoneError = error) }
    }

    private fun getProfile () = viewModelScope.launch {
        getProfileUseCase.invoke().collect{ response ->
            when(response){
                is CinemaxResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
                is CinemaxResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is CinemaxResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userName = response.value.name,
                            name = response.value.name,
                            email = response.value.email,
                            phoneNumber = response.value.phoneNumber,
                            photo = response.value.photo
                        )
                    }
                }
            }
        }
    }

    fun uploadPhoto(imageUri: Uri) = viewModelScope.launch {
        val userProfile = User(
            name = uiState.value.name,
            email = uiState.value.email,
            phoneNumber = uiState.value.phoneNumber,
            photo = uiState.value.photo
        )

        if(!imageUri.path.isNullOrEmpty()) {
            uploadImageUseCase.invoke(userProfile, imageUri).collect { result ->
                when (result) {
                    is CinemaxResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = result.error
                            )
                        }
                    }

                    is CinemaxResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is CinemaxResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = result.value
                            )
                        }
                    }
                }
            }
        }else{
            saveProfileUseCase.invoke(userProfile).collect{ response ->
                when(response){
                    is CinemaxResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                    is CinemaxResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is CinemaxResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = response.value
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteProfile() = viewModelScope.launch {
        if (!_uiState.value.photo.isNullOrEmpty()) {
            deleteProfileUseCase(_uiState.value.photo ?: "").collect { response ->
                when (response) {
                    is CinemaxResponse.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, userMessage = response.error)
                        }
                    }

                    is CinemaxResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is CinemaxResponse.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, userMessage = response.value)
                        }
                    }
                }
            }
        }else{
            _uiState.update { it.copy(userMessage = "Please Add Profile Picture") }
        }
    }
}
