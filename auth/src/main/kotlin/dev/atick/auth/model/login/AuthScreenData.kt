package dev.atick.auth.model.login

import android.content.IntentSender
import dev.atick.core.ui.utils.TextFiledData

data class AuthScreenData(
    val name: TextFiledData = TextFiledData(String()),
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
    val googleSignInIntent: IntentSender? = null
)
