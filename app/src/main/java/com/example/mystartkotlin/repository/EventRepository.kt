package com.example.mystartkotlin.repository

import androidx.annotation.WorkerThread
import com.example.mystartkotlin.datasource.room.Event
import com.example.mystartkotlin.datasource.room.EventDao
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val allEvents: Flow<List<Event>> = eventDao.getAllEventsUsingFlow()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    @Suppress
    @WorkerThread
    suspend fun deleteAll() {
        eventDao.deleteAll()
    }

    //@Suppress
    //@WorkerThread
    suspend fun deleteByEventId(id: Int) {
        eventDao.deleteByEventId(id)
    }

    val countEvents: Flow<Int> = eventDao.getCountEvents()

}