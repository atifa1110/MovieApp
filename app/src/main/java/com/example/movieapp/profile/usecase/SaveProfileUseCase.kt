package com.example.movieapp.profile.usecase

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(user: User) : Flow<CinemaxResponse<String>> {
        return authRepository.saveProfile(user)
    }
}