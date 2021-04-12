package com.example.mystartkotlin.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mystartkotlin.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

@Database(entities = [Event::class], version = 1)
abstract class EventRoomDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): EventRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventRoomDatabase::class.java,
                        "event_database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(EventDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        class EventDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.eventDao())

                    }
                }
            }
        }

        suspend fun populateDatabase(eventDao: EventDao) {
            eventDao.deleteAll()
            val event = Event(null,
                    R.string.defaultNumber.toString(),
                    R.string.defaultTextDescription.toString(),
                    R.string.defaultDateTime.toString())
            eventDao.insert(event)
        }
    }
}