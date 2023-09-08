package dev.atick.auth.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.auth.model.login.LoginScreenData
import dev.atick.core.extensions.isEmailValid
import dev.atick.core.extensions.isPasswordValid
import dev.atick.core.ui.utils.TextFiledData
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _loginUiState: MutableStateFlow<UiState<LoginScreenData>> =
        MutableStateFlow(UiState.Success(LoginScreenData()))
    val loginUiState = _loginUiState.asStateFlow()

    fun updateEmail(email: String) {
        _loginUiState.update {
            UiState.Success(
                it.data.copy(
                    email = TextFiledData(
                        value = email,
                        errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                    ),
                ),
            )
        }
    }

    fun updatePassword(password: String) {
        _loginUiState.update {
            UiState.Success(
                it.data.copy(
                    password = TextFiledData(
                        value = password,
                        errorMessage = if (password.isPasswordValid()) null else "Password Not Valid",
                    ),
                ),
            )
        }
    }

    fun loginWithEmailAndPassword() {

    }

}