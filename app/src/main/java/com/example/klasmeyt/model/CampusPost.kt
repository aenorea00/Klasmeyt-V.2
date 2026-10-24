package com.example.klasmeyt.model

data class CampusPost(
    val id: String = "",
    val title: String = "",
    val category: String = "General",
    val content: String = "",
    val author: String = "Anonymous",
    val authorStudentNumber: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: String = "JUST NOW"
)