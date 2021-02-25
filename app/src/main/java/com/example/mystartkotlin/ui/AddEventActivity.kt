package com.example.mystartkotlin.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mystartkotlin.EventViewModel
import com.example.mystartkotlin.EventViewModelFactory
import com.example.mystartkotlin.dependency.EventsApplication
import com.example.mystartkotlin.R
import com.example.mystartkotlin.datasource.room.Event
import java.lang.RuntimeException
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

    enum class Fragments {
        NUMBER, DESCRIPTION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_add)

        initializeView()

    }

    // Метод обработки нажатия на кнопку Отменить
    fun goToMainActivityWithoutEvent(view: View?) {
        stopProgressFragments()
        finish()
    }

    //Метод обработки нажатия на кнопку Подтвердить
    @SuppressLint("SetTextI18n")
    fun goToMainActivityWithEvent(view: View?) {
        //initializeView()
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

        textListener(noteNumber, Fragments.NUMBER)
        textListener(noteDescription, Fragments.DESCRIPTION)

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
        stopProgressFragments()
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

    private fun stopProgressFragments() {
        val fragmentNumber: ProgressFragment = supportFragmentManager.findFragmentById(R.id.fragmentNumber) as ProgressFragment
        fragmentNumber.threadProgress.interrupt()
        val fragmentDescription: ProgressFragment = supportFragmentManager.findFragmentById(R.id.fragmentDescription) as ProgressFragment
        fragmentDescription.threadProgress.interrupt()

    }

    private fun textListener(text: EditText, fragments: Fragments) {
        text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (fragments) {
                    Fragments.NUMBER -> {
                        val fragmentNumber: Fragment = FragmentManager.findFragment(findViewById(R.id.fragmentNumber))
                        fragmentNumber.view?.findViewById<ProgressBar>(R.id.indicator)?.progress = try {
                            s.toString().toInt()
                        } catch (e: RuntimeException) {
                            0
                        }
                    }
                    Fragments.DESCRIPTION -> {
                        val fragmentDescription: Fragment = FragmentManager.findFragment(findViewById(R.id.fragmentDescription))
                        fragmentDescription.view?.findViewById<ProgressBar>(R.id.indicator)?.progress = start
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}