package com.example.movieapp.register.usecase

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String, user : User) : Flow<CinemaxResponse<String>>{
        return authRepository.registerWithEmailAndPassword(email,password,user)
    }
}