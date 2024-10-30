package com.example.movieapp.login.usecase

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) : Flow<CinemaxResponse<String>>{
        return authRepository.signInWithEmailAndPassword(email,password)
    }
}