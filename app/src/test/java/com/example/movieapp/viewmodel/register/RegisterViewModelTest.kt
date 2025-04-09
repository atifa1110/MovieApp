package com.example.movieapp.viewmodel.register

import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.example.movieapp.register.presentation.RegisterViewModel
import com.example.movieapp.register.usecase.RegisterWithEmailAndPasswordUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase = mockk(relaxed = true)
    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // ✅ Inisialisasi MockK
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(registerWithEmailAndPasswordUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onNameChange with valid name should update name and clear error`() {
        val name = "John Doe"
        viewModel.onNameChange(name)
        assertEquals(name, viewModel.uiState.value.name)
        assertNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun `onNameChange with empty name should set error`() {
        viewModel.onNameChange("")
        assertEquals("Name cannot be empty", viewModel.uiState.value.nameError)
    }

    @Test
    fun `onEmailChange with valid email should update email and clear error`() {
        val email = "test@example.com"
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
        viewModel.onEmailChange("invalid_email")
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
    fun `registerEmailAndPassword with success should update state`() = runTest {
        val name = "John Doe"
        val email = "test@example.com"
        val password = "password123"
        val successMessage = "Registration successful"
        val user = User(name, email)

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { registerWithEmailAndPasswordUseCase.invoke(email, password, user) } returns flowOf(
            CinemaxResponse.Success(successMessage)
        )

        viewModel.registerEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(true, viewModel.uiState.value.isRegister)
        assertEquals(successMessage, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `registerEmailAndPassword with loading should update state`() = runTest {
        val name = "John Doe"
        val email = "test@example.com"
        val password = "password123"
        val user = User(name, email)

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { registerWithEmailAndPasswordUseCase.invoke(email, password, user) } returns flowOf(
            CinemaxResponse.Loading
        )

        viewModel.registerEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.isLoading)
        assertEquals(false, viewModel.uiState.value.isRegister)
    }

    @Test
    fun `registerEmailAndPassword with failure should update state`() = runTest {
        val name = "John Doe"
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Registration failed"
        val user = User(name, email)

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { registerWithEmailAndPasswordUseCase.invoke(email, password, user) } returns flowOf(
            CinemaxResponse.Failure(errorMessage)
        )

        viewModel.registerEmailAndPassword()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(false, viewModel.uiState.value.isRegister)
        assertEquals(errorMessage, viewModel.uiState.value.userMessage)
    }

    @Test
    fun `snackBarMessageShown clears userMessage`() {
        viewModel.uiState.value.copy(userMessage = "Test Message")
        viewModel.snackBarMessageShown()
        assertNull(viewModel.uiState.value.userMessage)
    }

}