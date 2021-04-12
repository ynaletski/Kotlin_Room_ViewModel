package com.example.mystartkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystartkotlin.repository.EventRepository
import com.example.mystartkotlin.datasource.room.Event
import com.example.mystartkotlin.repository.IEventRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EventViewModel(private val repository: IEventRepository) : ViewModel() {

    val allEvents: LiveData<List<Event>> = repository.allEvents.asLiveData()

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteByEventId(id: Int) = viewModelScope.launch {
        repository.deleteByEventId(id)
    }

    val countEvents: LiveData<Int> = repository.countEvents.asLiveData()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}