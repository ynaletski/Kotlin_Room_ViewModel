package com.example.mystartkotlin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.auth.AuthPromptErrorException
import androidx.biometric.auth.AuthPromptFailureException
import androidx.biometric.auth.AuthPromptHost
import androidx.biometric.auth.Class3BiometricAuthPrompt
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystartkotlin.*
import com.example.mystartkotlin.biometric.BiometricCipher
import com.example.mystartkotlin.biometric.authenticate
import com.example.mystartkotlin.dependency.EventsApplication
import com.example.mystartkotlin.datasource.room.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

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
                eventViewModel.insert(
                    Event(
                        null,
                        resources.getString(R.string.defaultNumber),
                        resources.getString(R.string.defaultTextDescription),
                        resources.getString(R.string.defaultDateTime)
                    )
                )
            }
        }

        deleteAll.setOnClickListener {
            deleteConfirmBiometricAuth()
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

    private fun deleteConfirmAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity).apply {
            this.setTitle(R.string.dialogTitle)
                .setMessage(R.string.dialogMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(applicationContext, "удаление", Toast.LENGTH_LONG).show()
                    eventViewModel.deleteAll()
                }
                .setNeutralButton("Cancel") { _, _ ->
                    Toast.makeText(applicationContext, "действие отменено", Toast.LENGTH_LONG)
                        .show()
                }.create()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    private fun deleteConfirmBiometricAuth() {
        val success = BiometricManager.from(this)
            .canAuthenticate(BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS

        if (success) {
            val biometricCipher = BiometricCipher(this.applicationContext)
            val encryptor = biometricCipher.getEncryptor()

            val authPrompt = Class3BiometricAuthPrompt.Builder("Strong biometry", "dismiss").apply {
                setSubtitle("Input your biometry")
                setDescription("We need your finger")
                setConfirmationRequired(true)
            }.build()

            lifecycleScope.launch {
                try {
                    val authResult =
                        authPrompt.authenticate(AuthPromptHost(this@MainActivity), encryptor)

                    val encryptedEntity = authResult.cryptoObject?.cipher?.let { cipher ->
                        biometricCipher.encrypt(
                            "Secret data",
                            cipher
                        )
                    }
                    deleteConfirmAlertDialog()
                    //Log.d(MainActivity::class.simpleName, String(encryptedEntity!!.ciphertext))
                } catch (e: AuthPromptErrorException) {
                    Log.e("AuthPromptError", e.message ?: "no message")
                } catch (e: AuthPromptFailureException) {
                    Log.e("AuthPromptFailure", e.message ?: "no message")
                }
            }
        }
    }

}