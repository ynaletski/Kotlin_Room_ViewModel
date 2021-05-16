package com.example.mystartkotlin.datasource.repository

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl : HelloRepository {
    override fun giveHello() = "HELLO"
}