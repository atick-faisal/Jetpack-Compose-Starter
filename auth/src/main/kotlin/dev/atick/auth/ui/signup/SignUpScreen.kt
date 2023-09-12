package dev.atick.auth.ui.signup

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.atick.auth.R
import dev.atick.auth.model.login.AuthScreenData
import dev.atick.auth.ui.AuthViewModel
import dev.atick.core.ui.component.JetpackButton
import dev.atick.core.ui.component.JetpackOutlinedButton
import dev.atick.core.ui.component.JetpackTextButton
import dev.atick.core.ui.utils.DevicePreviews
import dev.atick.core.ui.utils.StatefulComposable

@Composable
fun SignUpRoute(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val authState by authViewModel.authUiState.collectAsState()
    val googleSignInIntent = authState.data.googleSignInIntent

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.run {
                    authViewModel.signInWithIntent(this)
                }
            }
        },
    )

    googleSignInIntent?.let { intentSender ->
        LaunchedEffect(key1 = googleSignInIntent) {
            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
        }
    }

    StatefulComposable(
        state = authState,
        onShowSnackbar = onShowSnackbar,
    ) { authScreenData ->
        SignUpScreen(
            authScreenData,
            authViewModel::updateName,
            authViewModel::updateEmail,
            authViewModel::updatePassword,
            authViewModel::getGoogleSignInIntent,
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
    onSignUpWithGoogleClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(stringResource(id = R.string.sign_up), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        JetpackOutlinedButton(
            onClick = onSignUpWithGoogleClick,
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
        OutlinedTextField(
            value = authScreenData.name.value,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.name),
                )
            },
            shape = RoundedCornerShape(percent = 50),
        )
        AnimatedVisibility(visible = authScreenData.name.errorMessage != null) {
            Text(
                text = authScreenData.name.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
            )
        }
        OutlinedTextField(
            value = authScreenData.email.value,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
            shape = RoundedCornerShape(percent = 50),
        )
        AnimatedVisibility(visible = authScreenData.email.errorMessage != null) {
            Text(
                text = authScreenData.email.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
            )
        }
        // Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = authScreenData.password.value,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.password),
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description =
                    if (passwordVisible) stringResource(R.string.hide_password)
                    else stringResource(R.string.show_password)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            shape = RoundedCornerShape(percent = 50),
        )
        AnimatedVisibility(visible = authScreenData.password.errorMessage != null) {
            Text(
                text = authScreenData.password.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
            )
        }
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
        onSignUpWithGoogleClick = {},
        onSignUpClick = {},
        onSignInClick = {},
    )
}