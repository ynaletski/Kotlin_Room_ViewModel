package com.example.mystartkotlin.data.repository

import androidx.annotation.WorkerThread
import com.example.mystartkotlin.data.room.Event
import com.example.mystartkotlin.data.room.EventDao
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) : IEventRepository {

    override val allEvents: Flow<List<Event>> = eventDao.getAllEventsUsingFlow()

    @WorkerThread
    override suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    @WorkerThread
    override suspend fun deleteAll() {
        eventDao.deleteAll()
    }

    @WorkerThread
    override suspend fun deleteByEventId(id: Int) {
        eventDao.deleteByEventId(id)
    }

    override val countEvents: Flow<Int> = eventDao.getCountEvents()
}
