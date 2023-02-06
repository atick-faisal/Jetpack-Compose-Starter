package dev.atick.compose.ui.home

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        HomeScreen()
    }
}