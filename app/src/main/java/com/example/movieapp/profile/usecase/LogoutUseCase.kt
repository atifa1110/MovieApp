package com.example.movieapp.profile.usecase

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        return authRepository.logout()
    }
}