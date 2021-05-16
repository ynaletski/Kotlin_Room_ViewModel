package com.example.feature.biometric

data class EncryptedEntity(
        val cipherText: ByteArray,
        val initVector: ByteArray
)
