package com.example.feature.biometric

import androidx.biometric.BiometricPrompt
import androidx.biometric.auth.AuthPromptHost
import androidx.biometric.auth.Class3BiometricAuthPrompt
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun Class3BiometricAuthPrompt.authenticate(
    host: AuthPromptHost,
    crypto: BiometricPrompt.CryptoObject?,
): BiometricPrompt.AuthenticationResult {
    return suspendCancellableCoroutine { continuation ->
        val authPrompt = startAuthentication(
            host,
            crypto,
            Runnable::run,
            CoroutineAuthPromptCallback(continuation)
        )

        continuation.invokeOnCancellation {
            authPrompt.cancelAuthentication()
        }
    }
}
