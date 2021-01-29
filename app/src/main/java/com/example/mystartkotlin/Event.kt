package com.example.mystartkotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
        @PrimaryKey
        (autoGenerate = true) val id: Int?,

        @ColumnInfo
        (name = "number") var number: String?,

        @ColumnInfo
        (name = "description") var description: String?,

        @ColumnInfo
        (name = "dateTime") var dateTime: String?
)