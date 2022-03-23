package com.example.mystartkotlin.data.repository

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl : HelloRepository {
    override fun giveHello() = "HELLO"
}
