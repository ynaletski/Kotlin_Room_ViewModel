package com.example.mystartkotlin.biometric

data class EncryptedEntity(
        val cipherText: ByteArray,
        val initVector: ByteArray
)
