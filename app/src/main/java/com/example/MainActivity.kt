package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.ads.IronSourceAdManager
import com.example.ui.screens.RadioHomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.RadioViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RadioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        IronSourceAdManager.init(this)
        setContent {
            MyApplicationTheme {
                RadioHomeScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        IronSourceAdManager.onActivityResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSourceAdManager.onActivityPause(this)
    }
}

