package com.example.klasmeyt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.klasmeyt.navigation.AppNavGraph
import com.example.klasmeyt.ui.theme.KlasmeytTheme
import com.example.klasmeyt.viewmodel.FeedViewModel
import com.example.klasmeyt.viewmodel.FeedViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as KlasmeytApplication
        val feedViewModel: FeedViewModel by viewModels {
            FeedViewModelFactory(app.repository)
        }

        setContent {
            KlasmeytTheme {
                AppNavGraph(feedViewModel = feedViewModel)
            }
        }
    }
}