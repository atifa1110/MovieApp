package com.example.movieapp.core.network.repository

import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.movieapp.login.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource
) : AuthRepository {

    override fun getUserId(): FirebaseUser? {
        return authNetworkDataSource.getUserUid()
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<CinemaxResponse<String>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)

        val task = authNetworkDataSource.signInWithEmailAndPassword(email, password)

        // Use a listener for the task completion
        task.addOnCompleteListener { taskResult ->
            if (taskResult.isSuccessful) {
                // Emit success state
                trySend(CinemaxResponse.Success("Login successfully!")).isSuccess
            } else {
                // Emit failure state if not successful
                trySend(CinemaxResponse.Failure("Authentication failed, Check email and password")).isSuccess
            }
            close()
        }.addOnFailureListener { exception ->
            // Emit failure on exception
            trySend(CinemaxResponse.Failure("Authentication failed: ${exception.message}")).isSuccess
            close()
        }

        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

    // Function for Firebase authentication
    override fun registerFirebaseUser(email: String, password: String, user: User): Flow<CinemaxResponse<User>> = callbackFlow {
        trySend(CinemaxResponse.Loading)

        authNetworkDataSource.registerWithEmailAndPassword(email, password)
            .addOnCompleteListener { taskResult ->
                if (taskResult.isSuccessful) {
                    val firebaseUser = taskResult.result?.user
                    if (firebaseUser != null) {
                        user.userId = firebaseUser.uid

                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(user.name)
                            .build()

                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    trySend(CinemaxResponse.Success(user))
                                    close()
                                } else {
                                    trySend(CinemaxResponse.Failure(profileTask.exception?.message ?: "Failed to update profile"))
                                    close()
                                }
                            }
                    } else {
                        trySend(CinemaxResponse.Failure("Registration failed: Unable to retrieve FirebaseUser."))
                        close()
                    }
                } else {
                    val errorMessage = when (val exception = taskResult.exception) {
                        is FirebaseAuthWeakPasswordException -> "Password should be at least 8 characters"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email entered"
                        is FirebaseAuthUserCollisionException -> "Email already registered."
                        else -> exception?.message ?: "Unexpected Error"
                    }
                    trySend(CinemaxResponse.Failure(errorMessage))
                    close()
                }
            }
            .addOnFailureListener { e ->
                trySend(CinemaxResponse.Failure(e.localizedMessage ?: "Unexpected Error"))
                close()
            }

        awaitClose()
    }

    // Function for saving user data to the database
    override fun saveUserData(user: User): Flow<CinemaxResponse<String>> = callbackFlow {
        trySend(CinemaxResponse.Loading)

        user.userId?.let { userId ->
            authNetworkDataSource.saveUser(userId, user)
                .addOnSuccessListener {
                    trySend(CinemaxResponse.Success("Registration successful"))
                    close()
                }
                .addOnFailureListener { e ->
                    trySend(CinemaxResponse.Failure(e.localizedMessage ?: "Failed to save user data"))
                    close()
                }
        } ?: run {
            trySend(CinemaxResponse.Failure("User ID is missing"))
            close()
        }
        awaitClose ()
    }

    // Combined registration function
    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        user: User
    ): Flow<CinemaxResponse<String>> = callbackFlow {
        registerFirebaseUser(email, password, user).collect { firebaseResponse ->
            when (firebaseResponse) {
                is CinemaxResponse.Success -> {
                    saveUserData(firebaseResponse.value).collect { saveResponse ->
                        when (saveResponse) {
                            is CinemaxResponse.Success -> {
                                trySend(CinemaxResponse.Success(saveResponse.value))
                                close()
                            }

                            is CinemaxResponse.Failure -> {
                                trySend(CinemaxResponse.Failure(saveResponse.error))
                                close()
                            }

                            is CinemaxResponse.Loading -> {
                                trySend(CinemaxResponse.Loading)
                            }
                        }
                    }
                }

                is CinemaxResponse.Failure -> {
                    trySend(CinemaxResponse.Failure(firebaseResponse.error))
                    close()
                }

                is CinemaxResponse.Loading -> {
                    trySend(CinemaxResponse.Loading)
                }
            }
        }
        awaitClose()
    }

    override fun logout() {
        authNetworkDataSource.signOut()
    }

    override fun getProfile(): Flow<CinemaxResponse<User>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)
        // Get the user UID (assuming it's non-null)
        val userUid = authNetworkDataSource.getUserUid()?.uid ?: ""
        val task = authNetworkDataSource.getProfile(userUid)

        task.addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                // Iterate through the documents in the "info" collection
                for (documentSnapshot in querySnapshot.documents) {
                    // You can access each document's data here
                    val user = documentSnapshot.toObject(User::class.java)?:User()
                    trySend(CinemaxResponse.Success(user))
                }
            }else{
                trySend(CinemaxResponse.Failure("No user data found"))
            }
            close()
        }.addOnFailureListener {
            trySend(CinemaxResponse.Failure("Error fetching user data"))
            close()
        }

        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

    override fun saveProfile(user: User): Flow<CinemaxResponse<String>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)

        val userUid = authNetworkDataSource.getUserUid()?.uid ?: ""
        val task = authNetworkDataSource.saveProfile(userUid, user).addOnSuccessListener { task->
            authNetworkDataSource.updateData(user.name,user.photo)
            trySend(CinemaxResponse.Success("Data is Save"))
            close()
        }.addOnFailureListener {
            trySend(CinemaxResponse.Failure("Data Failed To Save"))
            close()
        }
        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

}