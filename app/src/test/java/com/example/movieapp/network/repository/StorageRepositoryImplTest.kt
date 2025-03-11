package com.example.movieapp.network.repository

import android.net.Uri
import app.cash.turbine.test
import com.example.movieapp.MainCoroutineRule
import com.example.movieapp.core.database.source.WishlistDatabaseSource
import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.repository.StorageRepositoryImpl
import com.example.movieapp.core.network.repository.WishListRepositoryImpl
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StorageRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule() // For testing coroutines

    private lateinit var repository: StorageRepositoryImpl

    @Mock
    private lateinit var authNetworkDataSource: AuthNetworkDataSource

    @Mock
    private lateinit var storageReference: StorageReference

    @Mock
    private lateinit var documentReference: DocumentReference

    @Mock
    private lateinit var task: Task<Void>

    @Before
    fun setup() {
        repository = StorageRepositoryImpl(authNetworkDataSource)
    }


    @Test
    fun `saveProfile() should call authNetworkDataSource and return success`() = runTest {
        // Mock the necessary data and responses
        val user = User("test@example.com", "Test User", "")
        val firebaseUser = mock<FirebaseUser>()
        val userUid = "test_uid"

        // Mock the behavior of the AuthNetworkDataSource
        whenever(authNetworkDataSource.getUserUid()).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn(userUid)
        whenever(authNetworkDataSource.saveProfile(userUid, user)).thenReturn(task)
        whenever(task.addOnSuccessListener(any())).thenAnswer { invocation ->
            val onSuccessListener = invocation.arguments[0] as OnSuccessListener<Void>
            onSuccessListener.onSuccess(null)
            task
        }


        // Call the function and capture the result
        var result: CinemaxResponse<String>? = null
        repository.saveProfile(user) { response -> result = response }

        // Verify that saveProfile is called with the correct arguments
        verify(authNetworkDataSource).saveProfile(userUid, user)

        // Assert that the result is successful
        assert(result is CinemaxResponse.Success)
        assertEquals("Data is Saved Successfully",(result as CinemaxResponse.Success).value)
    }

    @Test
    fun `deleteProfileFromStorage - success`() = runTest {
        val mockStorageRef: StorageReference = mock()
        val mockStorageTask: Task<Void> = mock()
        val mockAuthNetworkDataSource = mock<AuthNetworkDataSource>()
        val mockDatabaseRef: DocumentReference = mock() // Mock DocumentReference
        val mockDatabaseTask: Task<Void> = mock()

        whenever(mockAuthNetworkDataSource.deleteStorage("testUrl")).thenReturn(mockStorageRef)
        whenever(mockStorageRef.delete()).thenReturn(mockStorageTask)
        whenever(mockStorageTask.addOnSuccessListener(any())).thenAnswer { invocation ->
            val listener = invocation.getArgument(0) as (Void?) -> Unit
            listener.invoke(null)
            mockStorageTask
        }

        // Mock the data source's interaction with the database:
        whenever(mockAuthNetworkDataSource.removeProfileDatabase()).thenReturn(mockDatabaseRef) // Return the mock DocumentReference
        whenever(mockDatabaseRef.update(any())).thenReturn(mockDatabaseTask) // Mock the update() call on the DocumentReference
        whenever(mockDatabaseTask.addOnSuccessListener(any())).thenAnswer { invocation ->
            val listener = invocation.getArgument(0) as (Void?) -> Unit
            listener.invoke(null)
            mockDatabaseTask
        }

        repository.deleteProfileFromStorage("testUrl").test {
            assertEquals(CinemaxResponse.Loading, awaitItem())
            assertEquals(CinemaxResponse.Success("Image Deleted Successfully from Storage"), awaitItem())
            assertEquals(CinemaxResponse.Success("Image URL Removed Successfully from Firestore"), awaitItem())
            awaitComplete()
        }

        verify(mockAuthNetworkDataSource).deleteStorage("testUrl")
        verify(mockStorageRef).delete()
        verify(mockAuthNetworkDataSource).removeProfileDatabase() // Verification on the Datasource!
    }


}