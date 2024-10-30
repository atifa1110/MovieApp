package com.example.movieapp.register.usecase

import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserDatabaseUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(user: User, result: (CinemaxResponse<String>) -> Unit) {
        return authRepository.updateUserInfo(user,result)
    }
}