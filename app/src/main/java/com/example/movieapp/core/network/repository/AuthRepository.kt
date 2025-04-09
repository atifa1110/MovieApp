package com.example.movieapp.core.network.repository

import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUserId() : FirebaseUser?

    fun signInWithEmailAndPassword(email: String, password: String) : Flow<CinemaxResponse<String>>

    fun registerFirebaseUser(email: String, password: String, user: User): Flow<CinemaxResponse<User>>

    fun saveUserData(user: User): Flow<CinemaxResponse<String>>

    fun registerWithEmailAndPassword(email: String, password: String, user : User) : Flow<CinemaxResponse<String>>

    fun logout()
    fun getProfile() : Flow<CinemaxResponse<User>>
    fun saveProfile(user: User) : Flow<CinemaxResponse<String>>
}