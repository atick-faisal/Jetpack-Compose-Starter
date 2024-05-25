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

package dev.atick.auth.ui.signup

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.atick.auth.R
import dev.atick.auth.models.AuthScreenData
import dev.atick.auth.ui.AuthViewModel
import dev.atick.core.extensions.getActivity
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.components.JetpackOutlinedButton
import dev.atick.core.ui.components.JetpackPasswordFiled
import dev.atick.core.ui.components.JetpackTextButton
import dev.atick.core.ui.components.JetpackTextFiled
import dev.atick.core.ui.utils.DevicePreviews
import dev.atick.core.ui.utils.StatefulComposable

@Composable
fun SignUpRoute(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val authState by authViewModel.authUiState.collectAsState()
//    val googleSignInIntent = authState.data.googleSignInIntent

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult(),
//        onResult = { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.run {
//                    authViewModel.signInWithIntent(this)
//                }
//            }
//        },
//    )
//
//    googleSignInIntent?.let { intentSender ->
//        LaunchedEffect(key1 = googleSignInIntent) {
//            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
//        }
//    }

    StatefulComposable(
        state = authState,
        onShowSnackbar = onShowSnackbar,
    ) { authScreenData ->
        SignUpScreen(
            authScreenData,
            authViewModel::updateName,
            authViewModel::updateEmail,
            authViewModel::updatePassword,
            authViewModel::registerWithGoogle,
            authViewModel::registerWithEmailAndPassword,
            onSignInClick,
        )
    }
}

@Composable
private fun SignUpScreen(
    authScreenData: AuthScreenData,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterWithGoogleClick: (Activity) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current.getActivity()

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(stringResource(id = R.string.sign_up), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        JetpackOutlinedButton(
            onClick = { activity?.run { onRegisterWithGoogleClick.invoke(this) } },
            text = { Text(text = stringResource(R.string.sign_up_with_google)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
        )
        Text(text = "or", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        JetpackTextFiled(
            value = authScreenData.name.value,
            errorMessage = authScreenData.name.errorMessage,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.name),
                )
            },
        )
        JetpackTextFiled(
            value = authScreenData.email.value,
            errorMessage = authScreenData.email.errorMessage,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
        )
        JetpackPasswordFiled(
            value = authScreenData.password.value,
            errorMessage = authScreenData.password.errorMessage,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.password),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackButton(
            onClick = {
                focusManager.clearFocus()
                onSignUpClick.invoke()
            },
            modifier = Modifier.fillMaxWidth(),
            text = { Text(stringResource(R.string.sign_up)) },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.already_have_an_account))
            JetpackTextButton(onClick = onSignInClick) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        authScreenData = AuthScreenData(),
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onRegisterWithGoogleClick = {},
        onSignUpClick = {},
        onSignInClick = {},
    )
}
