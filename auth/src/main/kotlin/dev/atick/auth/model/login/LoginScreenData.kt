package dev.atick.auth.model.login

import dev.atick.core.ui.utils.TextFiledData

data class LoginScreenData(
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String())
)
