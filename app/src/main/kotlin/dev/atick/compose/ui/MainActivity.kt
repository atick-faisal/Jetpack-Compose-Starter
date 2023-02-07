package dev.atick.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.R
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.network.utils.NetworkUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    // ... At least one inject in @AndroidEntryPoint is required
    // ... to solve Hilt deprecation issue
    lateinit var networkUtils: NetworkUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContentView(R.layout.activity_main)

        collectWithLifecycle(networkUtils.currentState) {}
    }
}