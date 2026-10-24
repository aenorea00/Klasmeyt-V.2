package com.example.klasmeyt.data

import com.example.klasmeyt.model.CampusPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val postsCollection = firestore.collection("campus_posts")

    val allPosts: Flow<List<CampusPost>> = callbackFlow {
        val listener = postsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val posts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(CampusPost::class.java)?.copy(id = doc.id)
                }
                trySend(posts)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun insert(post: CampusPost) {
        postsCollection.add(post).await()
    }

    suspend fun update(post: CampusPost) {
        postsCollection.document(post.id).set(post).await()
    }

    suspend fun delete(postId: String) {
        postsCollection.document(postId).delete().await()
    }
}