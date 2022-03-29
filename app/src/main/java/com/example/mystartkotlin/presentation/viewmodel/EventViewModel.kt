package com.example.mystartkotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystartkotlin.data.cash.Cash
import com.example.mystartkotlin.data.repository.IEventRepository
import com.example.mystartkotlin.data.room.Event
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val STOP_TIMEOUT = 5000L

class EventViewModel(private val repository: IEventRepository) : ViewModel() {
    val allEvents: StateFlow<List<Event>> = repository.allEvents.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STOP_TIMEOUT),
        initialValue = Cash.getEvents()
    )
    // livedata
    // val allEvents: LiveData<List<Event>> = repository.allEvents.asLiveData()

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteByEventId(id: Int) = viewModelScope.launch {
        repository.deleteByEventId(id)
    }

    val countEvents: StateFlow<Int> = repository.countEvents.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STOP_TIMEOUT),
        initialValue = 1
    )
    // livedata
    // val countEvents: LiveData<Int> = repository.countEvents.asLiveData()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
