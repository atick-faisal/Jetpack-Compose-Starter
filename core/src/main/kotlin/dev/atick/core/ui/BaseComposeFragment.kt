package dev.atick.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dev.atick.core.ui.theme.ComposeTheme

abstract class BaseComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            observeStates()
            setContent {
                ComposeTheme {
                    ComposeUi()
                }
            }
        }
    }

    @Composable
    abstract fun ComposeUi()

    open fun observeStates() {}
}