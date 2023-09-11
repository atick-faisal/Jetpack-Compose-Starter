package dev.atick.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.auth.model.login.AuthScreenData
import dev.atick.auth.repository.AuthRepository
import dev.atick.core.extensions.isEmailValid
import dev.atick.core.extensions.isPasswordValid
import dev.atick.core.ui.utils.TextFiledData
import dev.atick.core.ui.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginUiState: MutableStateFlow<UiState<AuthScreenData>> =
        MutableStateFlow(UiState.Success(AuthScreenData()))
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
        viewModelScope.launch {
            authRepository.signInWithEmailAndPassword(
                email = loginUiState.value.data.email.value,
                password = loginUiState.value.data.email.value,
            )
        }
    }

}