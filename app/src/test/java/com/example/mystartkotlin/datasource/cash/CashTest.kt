package com.example.mystartkotlin.datasource.cash

import com.example.mystartkotlin.datasource.room.Event
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.E

class CashTest : TestCase() {

    private var events: ArrayList<Event> = ArrayList()

    @Before
    override fun setUp() {
        events.add(Event(0,"0","00","000"))
        events.add(Event(1,"1","11","111"))
    }

    @Test
    fun testGetEvents() {
        val eventsTwo: ArrayList<Event> = ArrayList()
        eventsTwo.add(Event(0,"0","00","000"))
        eventsTwo.add(Event(1,"1","11","111"))

        Assert.assertEquals(events, eventsTwo)
    }
}