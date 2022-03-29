package com.example.feature.biometric

import androidx.biometric.BiometricPrompt
import androidx.biometric.auth.AuthPromptCallback
import androidx.biometric.auth.AuthPromptErrorException
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resumeWithException

/**
 * Implementation of [AuthPromptCallback] used to transform callback results for coroutine APIs.
 */
internal class CoroutineAuthPromptCallback(
    private val continuation: CancellableContinuation<BiometricPrompt.AuthenticationResult>,
) : AuthPromptCallback() {
    override fun onAuthenticationError(
        activity: FragmentActivity?,
        errorCode: Int,
        errString: CharSequence,
    ) {
        continuation.resumeWithException(AuthPromptErrorException(errorCode, errString))
    }

    override fun onAuthenticationSucceeded(
        activity: FragmentActivity?,
        result: BiometricPrompt.AuthenticationResult,
    ) {
        continuation.resumeWith(Result.success(result))
    }

    override fun onAuthenticationFailed(activity: FragmentActivity?) {
        // continuation.resumeWithException(AuthPromptFailureException())
        // my stub
    }
}
