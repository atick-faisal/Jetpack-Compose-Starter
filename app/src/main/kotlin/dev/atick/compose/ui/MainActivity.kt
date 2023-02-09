package dev.atick.compose.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.R
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.extensions.checkForPermissions
import dev.atick.network.utils.NetworkUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    // ... At least one inject in @AndroidEntryPoint is required
    // ... to solve Hilt deprecation issue
    lateinit var networkUtils: NetworkUtils

    private val permissions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContentView(R.layout.activity_main)

        collectWithLifecycle(networkUtils.currentState) {}

        //                ... App Permissions ...
        // ----------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissions.add(Manifest.permission.CAMERA)
        checkForPermissions(permissions)
    }
}