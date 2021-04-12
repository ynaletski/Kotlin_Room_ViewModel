package com.example.mystartkotlin.repository

import androidx.annotation.WorkerThread
import com.example.mystartkotlin.datasource.room.Event
import com.example.mystartkotlin.datasource.room.EventDao
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) : IEventRepository {

    override val allEvents: Flow<List<Event>> = eventDao.getAllEventsUsingFlow()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    @Suppress
    @WorkerThread
    override suspend fun deleteAll() {
        eventDao.deleteAll()
    }

    @Suppress
    @WorkerThread
    override suspend fun deleteByEventId(id: Int) {
        eventDao.deleteByEventId(id)
    }

   override val countEvents: Flow<Int> = eventDao.getCountEvents()

}