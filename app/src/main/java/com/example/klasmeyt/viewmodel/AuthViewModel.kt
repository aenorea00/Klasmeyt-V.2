package com.example.klasmeyt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klasmeyt.data.AuthRepository
import com.example.klasmeyt.model.User
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var loginStudentNumber by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var loginErrorMessage by mutableStateOf<String?>(null)
    var isLoggingIn by mutableStateOf(false)

    var regStudentNumber by mutableStateOf("")
    var regPassword by mutableStateOf("")
    var regFullName by mutableStateOf("")
    var regUsername by mutableStateOf("")
    var regBio by mutableStateOf("")
    var regErrorMessage by mutableStateOf<String?>(null)
    var isRegistering by mutableStateOf(false)

    /**
     * Attempts login by verifying studentNumber and password against Firestore.
     */
    fun login(onSuccess: (User) -> Unit) {
        if (loginStudentNumber.isBlank() || loginPassword.isBlank()) {
            loginErrorMessage = "Please enter student number and password."
            return
        }

        isLoggingIn = true
        loginErrorMessage = null

        viewModelScope.launch {
            val result = repository.login(loginStudentNumber.trim(), loginPassword.trim())
            isLoggingIn = false
            result.onSuccess { user ->
                loginErrorMessage = null
                onSuccess(user)
            }.onFailure { exception ->
                loginErrorMessage = exception.message ?: "Login failed."
            }
        }
    }

    fun validateStep1(onNextStep: () -> Unit) {
        if (regStudentNumber.isBlank() || regPassword.isBlank()) {
            regErrorMessage = "Both student number and password are required."
            return
        }
        if (regPassword.length < 6) {
            regErrorMessage = "Password must be at least 6 characters."
            return
        }

        viewModelScope.launch {
            isRegistering = true
            regErrorMessage = null

            val isTaken = repository.isStudentNumberTaken(regStudentNumber.trim())
            isRegistering = false

            if (isTaken) {
                regErrorMessage = "Student number is already registered."
            } else {
                onNextStep()
            }
        }
    }

    fun completeRegistration(onSuccess: () -> Unit) {
        if (regFullName.isBlank() || regUsername.isBlank()) {
            regErrorMessage = "Full name and username are required."
            return
        }

        val newUser = User(
            studentNumber = regStudentNumber.trim(),
            password = regPassword.trim(),
            fullName = regFullName.trim(),
            username = regUsername.trim(),
            bio = regBio.trim()
        )

        isRegistering = true
        regErrorMessage = null

        viewModelScope.launch {
            val result = repository.registerUser(newUser)
            isRegistering = false
            result.onSuccess {
                regStudentNumber = ""
                regPassword = ""
                regFullName = ""
                regUsername = ""
                regBio = ""
                regErrorMessage = null
                onSuccess()
            }.onFailure { error ->
                regErrorMessage = error.message ?: "Registration failed."
            }
        }
    }
}