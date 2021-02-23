package com.example.mystartkotlin.dependency

import android.app.Application
import com.example.mystartkotlin.repository.EventRepository
import com.example.mystartkotlin.datasource.room.EventRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class EventsApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { EventRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { EventRepository(database.eventDao()) }

}