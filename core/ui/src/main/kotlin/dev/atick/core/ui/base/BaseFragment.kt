/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dev.atick.core.ui.theme.JetpackTheme

/**
 * A base fragment class that provides common functionality for fragments using Jetpack Compose.
 */
abstract class BaseFragment : Fragment() {

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeStates()
    }

    /**
     * Called to create the fragment's view.
     *
     * @param inflater The layout inflater.
     * @param container The parent view.
     * @param savedInstanceState The saved instance state.
     * @return The created view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                JetpackTheme(dynamicColor = false) {
                    ComposeUi()
                }
            }
        }
    }

    /**
     * Defines the Jetpack Compose UI of the fragment.
     */
    @Composable
    abstract fun ComposeUi()

    /**
     * Observes the states relevant to the fragment.
     * This method can be overridden by subclasses to implement state observation.
     */
    open fun observeStates() {}
}
