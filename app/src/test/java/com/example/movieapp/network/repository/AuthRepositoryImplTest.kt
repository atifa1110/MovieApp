package com.example.movieapp.network.repository

import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.repository.AuthRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authNetworkDataSource: AuthNetworkDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authNetworkDataSource = mock(AuthNetworkDataSource::class.java)
        authRepository = AuthRepositoryImpl(authNetworkDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserId should return FirebaseUser`() {
        // Arrange
        val mockUser = mock<FirebaseUser>()
        whenever(authNetworkDataSource.getUserUid()).thenReturn(mockUser)

        // Act
        val result = authRepository.getUserId()

        // Assert
        assertEquals(mockUser, result)
    }

    @Test
    fun `signInWithEmailAndPassword should emit success when authentication succeeds`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val mockTask = mock<Task<AuthResult>>()

        // Pastikan `authNetworkDataSource` mengembalikan `mockTask`
        whenever(authNetworkDataSource.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)

        whenever(mockTask.isSuccessful).thenReturn(true)

        // Pastikan `mockTask` tidak null dan memanggil `onComplete()`
        whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
            (it.arguments[0] as OnCompleteListener<AuthResult>).onComplete(mockTask)
            mockTask
        }

        // Jalankan flow dan ambil hasilnya sebagai list
        val result = authRepository.signInWithEmailAndPassword(email, password).toList()

        advanceUntilIdle() // Memastikan semua coroutine selesai

        // Verifikasi hasil
        assert(result[0] is CinemaxResponse.Loading) // ✅ Pastikan pertama Loading
        assert(result[1] is CinemaxResponse.Success) // ✅ Pastikan kedua Success
        assertEquals("Login successfully!", (result[1] as CinemaxResponse.Success).value)
    }

    @Test
    fun `signInWithEmailAndPassword should emit failure when authentication fails`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val mockTask = mock<Task<AuthResult>>()

        whenever(authNetworkDataSource.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)
        whenever(mockTask.isSuccessful).thenReturn(false) // ❌ Task gagal
        whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
            (it.arguments[0] as OnCompleteListener<AuthResult>).onComplete(mockTask)
            mockTask
        }

        val result = authRepository.signInWithEmailAndPassword(email, password).toList()

        // Verifikasi hasil
        assert(result[0] is CinemaxResponse.Loading) // ✅ Loading pertama
        assert(result[1] is CinemaxResponse.Failure) // ✅ Failure kedua
        assertEquals("Authentication failed, Check email and password", (result[1] as CinemaxResponse.Failure).error)
    }

    @Test
    fun `signInWithEmailAndPassword should emit failure when exception occurs`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val mockTask = mock<Task<AuthResult>>()

        whenever(authNetworkDataSource.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)
        whenever(mockTask.addOnCompleteListener(any())).thenReturn(mockTask)
        whenever(mockTask.addOnFailureListener(any())).thenAnswer {
            (it.arguments[0] as OnFailureListener).onFailure(Exception("Network error"))
            mockTask
        }

        val result = authRepository.signInWithEmailAndPassword(email, password).toList()

        advanceUntilIdle()

        assert(result[0] is CinemaxResponse.Loading) // ✅ Loading pertama
        assert(result[1] is CinemaxResponse.Failure) // ✅ Failure kedua
        assertEquals("Authentication failed: Network error", (result[1] as CinemaxResponse.Failure).error)
    }

    @Test
    fun `registerWithEmailAndPassword should emit success when registration succeeds`() = runTest {
        // Mock input values
        val email = "newuser@example.com"
        val password = "password123"
        val user = User(name = "Test User") // 🔥 Ensure `user` is mocked

        // Mock dependencies
        val mockTask = mock<Task<AuthResult>>()
        val mockFirebaseUser = mock<FirebaseUser>()
        val mockProfileUpdateTask = mock<Task<Void>>()

        // 🔥 Ensure `authNetworkDataSource` is a mock
        whenever(authNetworkDataSource.registerWithEmailAndPassword(email, password)).thenReturn(mockTask)

        // Mock behavior for successful registration
        whenever(mockTask.isSuccessful).thenReturn(true)
        whenever(mockTask.result).thenReturn(mock())
        whenever(mockTask.result?.user).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("user123")
        whenever(mockFirebaseUser.updateProfile(any())).thenReturn(mockProfileUpdateTask)
        whenever(mockProfileUpdateTask.isSuccessful).thenReturn(true)

        // Mock updateUserInfo function
        whenever(authRepository.updateUserInfo(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(CinemaxResponse<String>) -> Unit>(1)
            callback(CinemaxResponse.Success("User registered successfully!"))
            Unit // Ensure it returns the correct type
        }


        // Collect results
        val result = authRepository.registerWithEmailAndPassword(email, password, user).toList()

        advanceUntilIdle() // Ensure all coroutines finish

        // Assertions
        assert(result[0] is CinemaxResponse.Loading) // First emission must be Loading
        assert(result[1] is CinemaxResponse.Success) // Second emission must be Success
        assertEquals("User registered successfully!", (result[1] as CinemaxResponse.Success).value)
    }

    @Test
    fun `logout should call authNetworkDataSource signOut`() {
        authRepository.logout()
        verify(authNetworkDataSource).signOut()
    }

    @Test
    fun `getProfile should emit user data when retrieval is successful`() = runTest {
        // Mock user data
        val mockUser = User(name = "Test User", userId = "user123")
        val mockFirebaseUser = mock<FirebaseUser>()
        val mockTask = mock<Task<QuerySnapshot>>()
        val mockQuerySnapshot = mock<QuerySnapshot>()
        val mockDocument = mock<DocumentSnapshot>()

        // Mock authNetworkDataSource behavior
        whenever(authNetworkDataSource.getUserUid()).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("user123")
        whenever(authNetworkDataSource.getProfile("user123")).thenReturn(mockTask)

        // Simulate successful task completion
        whenever(mockTask.addOnSuccessListener(any())).thenAnswer {
            val listener = it.arguments[0] as OnSuccessListener<QuerySnapshot>
            listener.onSuccess(mockQuerySnapshot)
            mockTask
        }

        whenever(mockQuerySnapshot.isEmpty).thenReturn(false)
        whenever(mockQuerySnapshot.documents).thenReturn(listOf(mockDocument))
        whenever(mockDocument.toObject(User::class.java)).thenReturn(mockUser)

        // Collect Flow results
        val result = authRepository.getProfile().toList()

        advanceUntilIdle()

        // ✅ Verify emitted values
        assert(result[0] is CinemaxResponse.Loading) // First state: Loading
        assert(result[1] is CinemaxResponse.Success) // Second state: Success
        assertEquals("Test User", (result[1] as CinemaxResponse.Success).value.name) // Verify user data
    }

    @Test
    fun `getProfile should emit failure when user data is empty`() = runTest {
        val mockFirebaseUser = mock<FirebaseUser>()
        val mockTask = mock<Task<QuerySnapshot>>()
        val mockQuerySnapshot = mock<QuerySnapshot>()

        // Mock behavior
        whenever(authNetworkDataSource.getUserUid()).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("user123")
        whenever(authNetworkDataSource.getProfile("user123")).thenReturn(mockTask)

        // Simulate empty response
        whenever(mockTask.addOnSuccessListener(any())).thenAnswer {
            val listener = it.arguments[0] as OnSuccessListener<QuerySnapshot>
            listener.onSuccess(mockQuerySnapshot)
            mockTask
        }
        whenever(mockQuerySnapshot.isEmpty).thenReturn(true)

        val result = authRepository.getProfile().toList()

        // ✅ Verify emitted values
        assert(result[0] is CinemaxResponse.Loading)
        assert(result[1] is CinemaxResponse.Failure)
        assertEquals("No user data found", (result[1] as CinemaxResponse.Failure).error)
    }

    @Test
    fun `getProfile should emit failure when fetching user data fails`() = runTest {
        val mockFirebaseUser = mock<FirebaseUser>()
        val mockTask = mock<Task<QuerySnapshot>>() // ✅ Mock Task<QuerySnapshot>

        // Mock behavior for user retrieval
        whenever(authNetworkDataSource.getUserUid()).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("user123")
        whenever(authNetworkDataSource.getProfile("user123")).thenReturn(mockTask)

        // Ensure addOnSuccessListener is stubbed first, even if it does nothing
        whenever(mockTask.addOnSuccessListener(any())).thenReturn(mockTask)

        // ✅ Simulate failure by returning mockTask and triggering failure listener
        whenever(mockTask.addOnFailureListener(any())).thenAnswer {
            val listener = it.arguments[0] as OnFailureListener
            listener.onFailure(Exception("Error fetching user data")) // ✅ Simulate error
            mockTask
        }

        // Collect the emitted Flow values
        val result = authRepository.getProfile().toList()

        // ✅ Verify emitted values
        assert(result[0] is CinemaxResponse.Loading) // First state: Loading
        assert(result[1] is CinemaxResponse.Failure) // Second state: Failure
        assertEquals("Error fetching user data", (result[1] as CinemaxResponse.Failure).error) // Verify error message
    }

    @Test
    fun `saveProfile should emit success when save operation is successful`() = runTest {
        // Mock User object
        val mockUser = User(name = "Test User")
        val mockFirebaseUser = mock<FirebaseUser>()
        val mockTask = mock<Task<Void>>() // Mock Task<Void> for Firebase

        // Mock getting the current user UID
        whenever(authNetworkDataSource.getUserUid()).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("user123")

        // Mock saveProfile behavior
        whenever(authNetworkDataSource.saveProfile("user123", mockUser)).thenReturn(mockTask)

        // Simulate successful Firebase operation
        whenever(mockTask.addOnSuccessListener(any())).thenAnswer {
            val listener = it.arguments[0] as OnSuccessListener<Void>
            listener.onSuccess(null) // ✅ Simulate success
            mockTask
        }

        // Prevent NullPointerException for addOnFailureListener
        whenever(mockTask.addOnFailureListener(any())).thenAnswer {
            mockTask
        }

        // Collect Flow results
        val result = authRepository.saveProfile(mockUser).toList()

        // ✅ Verify emitted values
        assert(result[0] is CinemaxResponse.Loading) // First state: Loading
        assert(result[1] is CinemaxResponse.Success) // Second state: Success
        assertEquals("Data is Save", (result[1] as CinemaxResponse.Success).value)
    }


}