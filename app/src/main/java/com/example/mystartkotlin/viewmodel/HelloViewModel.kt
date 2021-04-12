package com.example.mystartkotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystartkotlin.repository.HelloRepository

class HelloViewModel(private val repo: HelloRepository) : ViewModel() {
    fun sayHello() = "${repo.giveHello()} from HelloViewModel"
    fun sayBye() = "GoodBye"
}