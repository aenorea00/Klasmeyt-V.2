package com.example.klasmeyt.model

data class Comment(
    val id: String = "",
    val postId: String = "",
    val author: String = "",
    val authorStudentNumber: String = "",
    val text: String = "",
    val timestamp: String = "JUST NOW"
)