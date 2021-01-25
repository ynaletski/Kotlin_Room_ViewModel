package com.example.mystartkotlin

import androidx.annotation.WorkerThread
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

    @Suppress
    @WorkerThread
    suspend fun deleteByEventId(id: Int) {
        eventDao.deleteByEventId(id)
    }

    val countEvents: Flow<Int> = eventDao.getCountEvents()

}