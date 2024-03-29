package com.example.mystartkotlin.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.mystartkotlin.data.room.Event

internal class EventDiffUtil(
    private val oldList: List<Event>,
    private val newList: List<Event>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEvent = oldList[oldItemPosition]
        val newEvent = newList[newItemPosition]
        return oldEvent.hashCode() == newEvent.hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEvent = oldList[oldItemPosition]
        val newEvent = newList[newItemPosition]
        return oldEvent == newEvent
    }
}
