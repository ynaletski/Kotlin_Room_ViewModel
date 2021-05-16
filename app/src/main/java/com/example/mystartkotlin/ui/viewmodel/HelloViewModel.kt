package com.example.mystartkotlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystartkotlin.datasource.repository.HelloRepository

class HelloViewModel(private val repo: HelloRepository) : ViewModel() {
    fun sayHello() = "${repo.giveHello()} from HelloViewModel"
    fun sayBye() = "GoodBye"
}