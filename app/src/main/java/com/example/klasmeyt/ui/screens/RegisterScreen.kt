package com.example.klasmeyt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klasmeyt.data.AuthRepository
import com.example.klasmeyt.model.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val authRepository = remember { AuthRepository() }
    val coroutineScope = rememberCoroutineScope()

    var currentStep by remember { mutableIntStateOf(1) }

    var studentNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == 2) {
                            currentStep = 1
                            errorMessage = null
                        } else {
                            onBackToLogin()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (currentStep == 1) "Step 1: Credentials" else "Step 2: Profile Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (currentStep == 1)
                        "Set up your university login info"
                    else
                        "How your classmates will see you",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (currentStep == 1) {
                    OutlinedTextField(
                        value = studentNumber,
                        onValueChange = { input ->
                            studentNumber = input
                            errorMessage = null
                        },
                        label = { Text("Student Number") },
                        placeholder = { Text("e.g., 202210123") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { input ->
                            password = input
                            errorMessage = null
                        },
                        label = { Text("Password") },
                        placeholder = { Text("Minimum 6 characters") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { input ->
                            fullName = input
                            errorMessage = null
                        },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g., Jan Aeron Azures") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { input ->
                            username = input
                            errorMessage = null
                        },
                        label = { Text("Username") },
                        placeholder = { Text("e.g., aechuu") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { input ->
                            bio = input
                            errorMessage = null
                        },
                        label = { Text("Short Bio") },
                        placeholder = { Text("e.g., 3rd Year BSIT Student") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        },
                        minLines = 3,
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (currentStep == 1) {
                            if (studentNumber.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter both student number and password."
                            } else if (password.length < 6) {
                                errorMessage = "Password must be at least 6 characters."
                            } else {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    val isTaken = authRepository.isStudentNumberTaken(studentNumber.trim())
                                    isLoading = false
                                    if (isTaken) {
                                        errorMessage = "This student number is already registered."
                                    } else {
                                        currentStep = 2
                                    }
                                }
                            }
                        } else {
                            if (fullName.isBlank() || username.isBlank()) {
                                errorMessage = "Please enter your full name and username."
                            } else {
                                isLoading = true
                                errorMessage = null
                                val newUser = User(
                                    studentNumber = studentNumber.trim(),
                                    password = password.trim(),
                                    fullName = fullName.trim(),
                                    username = username.trim(),
                                    bio = bio.trim()
                                )
                                coroutineScope.launch {
                                    val result = authRepository.registerUser(newUser)
                                    isLoading = false
                                    result.onSuccess {
                                        onRegistrationSuccess()
                                    }.onFailure { e ->
                                        errorMessage = e.message ?: "Failed to register account."
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (currentStep == 1) "Next" else "Complete Registration",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(onClick = onBackToLogin) {
                    Text(
                        text = "Already have an account? Sign In",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}