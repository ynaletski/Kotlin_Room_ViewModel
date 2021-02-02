package com.example.mystartkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val requestTwoAct = 1
    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((application as EventsApplication).repository)
    }

    private lateinit var deleteAll: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        eventAdapter = EventAdapter(this, object : RemoveClickListener {

            override fun removeEvent(positionEvent: Int) {
                eventViewModel.deleteByEventId(positionEvent)
            }
        })

        recyclerView.adapter = eventAdapter

        eventViewModel.allEvents.observe(this) { events ->
            events.let { eventAdapter.insertData(it) }
        }

        eventViewModel.countEvents.observe(this) { count ->
            if (count == 0) {
                eventViewModel.insert(Event(null,
                        resources.getString(R.string.defaultNumber),
                        resources.getString(R.string.defaultTextDescription),
                        resources.getString(R.string.defaultDateTime)))
            }
        }

        deleteAll.setOnClickListener {

            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                this.setTitle(R.string.dialogTitle)
                        .setMessage(R.string.dialogMessage)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes") { _, _ ->
                            Toast.makeText(applicationContext, "удаление", Toast.LENGTH_LONG).show()
                            eventViewModel.deleteAll()
                        }
                        .setNeutralButton("Cancel") { _, _ ->
                            Toast.makeText(applicationContext, "действие отменено", Toast.LENGTH_LONG).show()
                        }.create()
            }

            alertDialog.setCancelable(false)
            alertDialog.show()
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

    private fun initialize() {
        recyclerView = findViewById(R.id.eventList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //если знаем за ранее размер списка то true
        recyclerView.setHasFixedSize(false)
        deleteAll = findViewById(R.id.deleteAll)
    }

}