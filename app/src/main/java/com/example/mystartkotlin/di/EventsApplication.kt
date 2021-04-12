package com.example.mystartkotlin.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EventsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@EventsApplication)
            androidLogger()
            modules(eventModule)
        }
    }

    /*private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { EventRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { EventRepository(database.eventDao()) }*/


}