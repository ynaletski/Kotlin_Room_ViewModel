package com.example.mystartkotlin.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getAllEventsUsingFlow(): Flow<List<Event>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(event: Event)

    @Insert
    fun insertAll(vararg event: Event)

    @Delete
    fun delete(event: Event)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteByEventId(eventId: Int)

    @Query("SELECT COUNT(id) FROM events")
    fun getCountEvents(): Flow<Int>
}
