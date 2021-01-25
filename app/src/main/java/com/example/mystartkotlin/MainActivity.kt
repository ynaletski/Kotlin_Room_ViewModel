package com.example.mystartkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val requestTwoAct = 1
    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((application as EventsApplication).repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.eventList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //если знаем за ранее размер списка то true
        recyclerView.setHasFixedSize(false)
        eventAdapter = EventAdapter(this, object : RemoveClickListener {

            override fun removeEvent(positionEvent: Int) {
                eventViewModel.deleteByEventId(positionEvent)
                //eventViewModel.deleteAll()
            }
        })

        recyclerView.adapter = eventAdapter

        eventViewModel.allEvents.observe(this) { events ->
            events.let { eventAdapter.insertData(it) }
        }

        eventViewModel.countEvents.observe(this) { count ->
            //println(count)
            if (count == 0) {
                eventViewModel.insert(Event(null,
                        resources.getString(R.string.defaultNumber),
                        resources.getString(R.string.defaultTextDescription),
                        resources.getString(R.string.defaultDateTime)))
            }
        }
    }

    // Метод обработки нажатия на кнопку
    fun goToAddEventActivity(view: View?) {
        val intent = Intent(this, AddEventActivity::class.java)
        startActivityForResult(intent, requestTwoAct)
    }

    //метод принимающий результат со второй активити()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //eventAdapter.insertData(Cash.getEvents())
        }
    }
}