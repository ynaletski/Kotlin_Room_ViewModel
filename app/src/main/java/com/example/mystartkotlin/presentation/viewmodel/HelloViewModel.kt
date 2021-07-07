package com.example.mystartkotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystartkotlin.data.repository.HelloRepository

class HelloViewModel(private val repo: HelloRepository) : ViewModel() {
    fun sayHello() = "${repo.giveHello()} from HelloViewModel"
    fun sayBye() = "GoodBye"
}