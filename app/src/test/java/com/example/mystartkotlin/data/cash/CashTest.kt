package com.example.mystartkotlin.data.cash

import com.example.mystartkotlin.data.room.Event
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CashTest : TestCase() {

    private var events: ArrayList<Event> = ArrayList()

    @Before
    override fun setUp() {
        Cash.addEvent("0", "00", "000")
        Cash.addEvent("1", "11", "111")
        events.add(Event(0, "0", "00", "000"))
        events.add(Event(0, "1", "11", "111"))
    }

    @Test
    fun testGetEvents() {
        Assert.assertEquals(events, Cash.getEvents())
    }
}