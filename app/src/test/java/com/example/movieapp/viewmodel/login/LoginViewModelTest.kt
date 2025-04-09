package com.example.movieapp.viewmodel.login

import com.example.movieapp.core.datastore.DataStoreRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.presentation.LoginViewModel
import com.example.movieapp.login.usecase.SignInWithEmailAndPasswordUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val dataStoreRepository: DataStoreRepository = mockk(relaxed = true)
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase = mockk(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        Dispatchers.setMain(testDispatcher)

        viewModel = LoginViewModel(dataStoreRepository, signInWithEmailAndPasswordUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange with valid email should update email and clear error`() {
        val email = "atifafiorenza24@gmail.com"
        viewModel.onEmailChange(email)
        assertEquals(email, viewModel.uiState.value.email)
        assertNull(viewModel.uiState.value.emailError)
    }

    @Test
    fun `onEmailChange with empty email should set error`() {
        viewModel.onEmailChange("")
        assertEquals("Email cannot be empty", viewModel.uiState.value.emailError)
    }

    @Test
    fun `onEmailChange with invalid email should set error`() {
        viewModel.onEmailChange("atifafiorenza24")
        assertEquals("Invalid email format", viewModel.uiState.value.emailError)
    }

    @Test
    fun `onPasswordChange with valid password should update password and clear error`() {
        val password = "password123"
        viewModel.onPasswordChange(password)
        assertEquals(password, viewModel.uiState.value.password)
        assertNull(viewModel.uiState.value.passwordError)
    }

    @Test
    fun `onPasswordChange with empty password should set error`() {
        viewModel.onPasswordChange("")
        assertEquals("Password cannot be empty", viewModel.uiState.value.passwordError)
    }

    @Test
    fun `onPasswordChange with short password should set error`() {
        viewModel.onPasswordChange("short")
        assertEquals("Password must be at least 8 characters", viewModel.uiState.value.passwordError)
    }

    @Test
    fun `signInEmailAndPassword with success should update state and save login state`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val successMessage = "Login successful"

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { signInWithEmailAndPasswordUseCase.invoke(email, password) } returns flowOf(
            CinemaxResponse.Success(successMessage)
        )

        viewModel.signInEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(true, viewModel.uiState.value.isLogin)
        assertEquals(successMessage, viewModel.uiState.value.userMessage)
        coVerify { dataStoreRepository.saveOnLoginState(true) }
    }

    @Test
    fun `signInEmailAndPassword with loading should update state`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { signInWithEmailAndPasswordUseCase.invoke(email, password) } returns flowOf(
            CinemaxResponse.Loading
        )

        viewModel.signInEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.isLoading)
        assertEquals(false, viewModel.uiState.value.isLogin)
    }

    @Test
    fun `signInEmailAndPassword with failure should update state`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Login failed"

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { signInWithEmailAndPasswordUseCase.invoke(email, password) } returns flowOf(
            CinemaxResponse.Failure(errorMessage)
        )

        viewModel.signInEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(false, viewModel.uiState.value.isLogin)
        assertEquals(errorMessage, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `snackBarMessageShown clears userMessage`() {
        viewModel.uiState.value.copy(userMessage = "Test Message")
        viewModel.snackBarMessageShown()
        assertNull(viewModel.uiState.value.userMessage)
    }
}