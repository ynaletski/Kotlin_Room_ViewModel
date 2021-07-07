package com.example.mystartkotlin.data.repository

import com.example.mystartkotlin.data.room.Event
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    val allEvents: Flow<List<Event>>
    suspend fun insert(event: Event)
    suspend fun deleteAll()
    suspend fun deleteByEventId(id: Int)
    val countEvents: Flow<Int>
}