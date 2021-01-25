package com.example.mystartkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() {
    private lateinit var dateAndTime: EditText
    private lateinit var noteNumber: EditText
    private lateinit var noteDescription: EditText
    private lateinit var errorNumber: TextView
    private lateinit var errorDescription: TextView

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((application as EventsApplication).repository)
    }

    enum class Error {
        NUMB_SCALE, NUMB_NULL, DESCRIPTION_NULL, NO_ERROR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_add)
    }

    // Метод обработки нажатия на кнопку Отменить
    fun goToMainActivityWithoutEvent(view: View?) {
        finish()
    }

    //Метод обработки нажатия на кнопку Подтвердить
    @SuppressLint("SetTextI18n")
    fun goToMainActivityWithEvent(view: View?) {
        initializeView()
        when (validationOfData()) {
            Error.NO_ERROR -> sendDataToMainActivity()
            Error.NUMB_NULL -> errorNumber.text = resources.getString(R.string.errorNumbNull)
            Error.NUMB_SCALE -> errorNumber.text = resources.getString(R.string.errorNumbScale)
            Error.DESCRIPTION_NULL -> errorDescription.text =
                    resources.getString(R.string.errorDescriptionNull)
        }
    }

    //метод для инициализации вьюшек
    private fun initializeView() {
        noteNumber = findViewById(R.id.editTextNumb)
        noteDescription = findViewById(R.id.editTextDescriptor)
        dateAndTime = findViewById(R.id.editTextTimeDate)
        errorNumber = findViewById(R.id.errorNumb)
        errorDescription = findViewById(R.id.errorDesc)
        val date = Date()
        @SuppressLint("SimpleDateFormat") val formatForDate = SimpleDateFormat("hh:mm dd.MM.yyyy")
        dateAndTime.setText(formatForDate.format(date))
    }

    //функция для отправки данных на первый activity
    private fun sendDataToMainActivity() {

        val intent = Intent(this, MainActivity::class.java)

        eventViewModel.insert(Event(null,
                noteNumber.text.toString(),
                noteDescription.text.toString(),
                dateAndTime.text.toString()
        ))

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun validationOfData(): Error {
        return if (noteNumber.text.isNotEmpty()) {
            if (noteNumber.text.toString().toInt() < 1 ||
                    noteNumber.text.toString().toInt() > 1000
            ) {
                Error.NUMB_SCALE
            } else {
                if (noteDescription.text.isNotEmpty()) {
                    Error.NO_ERROR
                } else {
                    Error.DESCRIPTION_NULL
                }
            }
        } else {
            Error.NUMB_NULL
        }
    }
}