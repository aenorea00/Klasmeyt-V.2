package com.example.klasmeyt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klasmeyt.data.AuthRepository
import com.example.klasmeyt.data.PostRepository
import com.example.klasmeyt.model.CampusPost
import com.example.klasmeyt.model.Comment
import com.example.klasmeyt.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedViewModel(
    private val repository: PostRepository = PostRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    var currentUser by mutableStateOf<User?>(null)
        private set

    fun updateCurrentUser(user: User?) {
        currentUser = user
    }

    private val _posts = MutableStateFlow<List<CampusPost>>(emptyList())
    val posts: StateFlow<List<CampusPost>> = _posts.asStateFlow()

    private val _activeComments = MutableStateFlow<List<Comment>>(emptyList())
    val activeComments: StateFlow<List<Comment>> = _activeComments.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            repository.allPosts.collect { postList ->
                _posts.value = postList
            }
        }
    }

    fun getPostById(postId: String): CampusPost? {
        return _posts.value.find { it.id == postId }
    }

    suspend fun getUserProfile(studentNumber: String): User? {
        if (studentNumber == currentUser?.studentNumber) return currentUser
        return authRepository.getUserByStudentNumber(studentNumber).getOrNull()
    }

    fun addPost(title: String, category: String, content: String) {
        viewModelScope.launch {
            val user = currentUser
            val authorHandle = user?.username?.ifBlank { user.fullName } ?: "student"
            val newPost = CampusPost(
                title = title,
                category = category,
                content = content,
                author = authorHandle,
                authorStudentNumber = user?.studentNumber ?: "",
                likesCount = 0,
                commentsCount = 0,
                timestamp = "JUST NOW"
            )
            repository.insert(newPost)
        }
    }

    fun toggleLike(postId: String, currentlyLiked: Boolean) {
        viewModelScope.launch {
            val post = _posts.value.find { it.id == postId } ?: return@launch
            val delta = if (currentlyLiked) -1 else 1
            val updatedLikes = maxOf(0, post.likesCount + delta)
            val updatedPost = post.copy(likesCount = updatedLikes)
            repository.update(updatedPost)
        }
    }

    fun listenToComments(postId: String) {
        firestore.collection("posts").document(postId).collection("comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val commentsList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Comment::class.java)?.copy(id = doc.id)
                    }
                    _activeComments.value = commentsList
                }
            }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val user = currentUser
            val authorHandle = user?.username?.ifBlank { user.fullName } ?: "student"
            val newComment = Comment(
                postId = postId,
                author = authorHandle,
                authorStudentNumber = user?.studentNumber ?: "",
                text = text.trim(),
                timestamp = "JUST NOW"
            )

            try {
                firestore.collection("posts").document(postId)
                    .collection("comments").add(newComment).await()

                val post = _posts.value.find { it.id == postId }
                if (post != null) {
                    val updated = post.copy(commentsCount = post.commentsCount + 1)
                    repository.update(updated)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePost(id: String, newTitle: String, newCategory: String, newContent: String) {
        viewModelScope.launch {
            val existing = _posts.value.find { it.id == id }
            val updated = existing?.copy(
                title = newTitle,
                category = newCategory,
                content = newContent
            ) ?: CampusPost(
                id = id,
                title = newTitle,
                category = newCategory,
                content = newContent,
                author = currentUser?.username?.ifBlank { currentUser?.fullName } ?: "student",
                authorStudentNumber = currentUser?.studentNumber ?: ""
            )
            repository.update(updated)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.delete(postId)
        }
    }
}