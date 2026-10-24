package com.example.klasmeyt.data

import com.example.klasmeyt.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun isStudentNumberTaken(studentNumber: String): Boolean {
        return try {
            val snapshot = usersCollection.document(studentNumber).get().await()
            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun registerUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.studentNumber).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(studentNumber: String, password: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(studentNumber).get().await()
            if (!snapshot.exists()) {
                Result.failure(Exception("Account not found. Please register first."))
            } else {
                val user = snapshot.toObject(User::class.java)
                if (user != null && user.password == password) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Invalid password. Please try again."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByStudentNumber(studentNumber: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(studentNumber).get().await()
            if (snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Failed to parse user profile."))
                }
            } else {
                Result.failure(Exception("User not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}