package com.example.mystartkotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Event(val _number: String, val _description: String, val _dateTime: String)

@Entity(tableName = "events")
data class Event(
        @PrimaryKey
        (autoGenerate = true) val id: Int?,
        /* @ColumnInfo
             (name = "id") val id: Int?,*/

        @ColumnInfo
        (name = "number") var number: String?,

        @ColumnInfo
        (name = "description") var description: String?,

        @ColumnInfo
        (name = "dateTime") var dateTime: String?
)