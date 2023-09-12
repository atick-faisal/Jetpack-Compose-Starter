package dev.atick.auth.model

import android.content.IntentSender
import dev.atick.core.ui.utils.TextFiledData

/**
 * Data class representing the input data for an authentication screen.
 *
 * @param name The data for the user's name input field.
 * @param email The data for the user's email input field.
 * @param password The data for the user's password input field.
 * @param googleSignInIntent The [IntentSender] for initiating Google Sign-In, if available.
 */
data class AuthScreenData(
    val name: TextFiledData = TextFiledData(String()),
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
    val googleSignInIntent: IntentSender? = null
)
