package com.example.klasmeyt

import android.app.Application
import com.example.klasmeyt.data.PostRepository

class KlasmeytApplication : Application() {
    val repository by lazy { PostRepository() }
}