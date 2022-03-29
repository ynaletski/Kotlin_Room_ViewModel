package com.example.mystartkotlin.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.feature.biometric.BiometricCipher
import com.example.feature.biometric.authenticate
import com.example.mystartkotlin.R
import com.example.mystartkotlin.data.room.Event
import com.example.mystartkotlin.presentation.viewmodel.EventViewModel
import com.example.mystartkotlin.presentation.viewmodel.HelloViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // private val requestSecondActivity = 1

    private val helloViewModel: HelloViewModel by viewModel()

    private val eventViewModel: EventViewModel by viewModel()

    /*private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((application as EventsApplication).repository)
    }*/

    private lateinit var deleteAll: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity onCreate", helloViewModel.sayHello())

        initialize()

        eventAdapter = EventAdapter(
            this,
            object : RemoveClickListener {
                override fun removeEvent(positionEvent: Int) {
                    eventViewModel.deleteByEventId(positionEvent)
                }
            }
        )

        recyclerView.adapter = eventAdapter

        lifecycleScope.launch {
            lifecycleScope.launchWhenStarted {
                eventViewModel.allEvents.collect { events ->
                    events.let {
                        eventAdapter.insertData(it)
                    }
                }
            }
        }
        // LiveData
        /*eventViewModel.allEvents.observe(this) { events ->
            events.let { eventAdapter.insertData(it) }
        }*/

        lifecycleScope.launch {
            lifecycleScope.launchWhenStarted {
                eventViewModel.countEvents.collect { count ->
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
            }
        }
        // LiveData
        /*eventViewModel.countEvents.observe(this) { count ->
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
        }*/

        deleteAll.setOnClickListener {
            deleteConfirmBiometricAuth()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity onDestroy", helloViewModel.sayBye)
    }

    // Метод обработки нажатия на кнопку
    @Suppress("unused")
    fun goToAddEventActivity(@Suppress("UNUSED_PARAMETER") view: View?) {
        startResultFromSecondActivity.launch(Intent(this, AddEventActivity::class.java))
        /*val intent = Intent(this, AddEventActivity::class.java)
        startActivityForResult(intent, requestSecondActivity)*/
    }

    // метод принимающий результат со второй активити()
    private val startResultFromSecondActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                // eventAdapter.insertData(Cash.getEvents())
                Log.d("StartActivityForResult", "Activity.RESULT_OK")
            } else {
                Log.d("StartActivityForResult", "Activity.RESULT_NOT_OK")
            }
        }
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //eventAdapter.insertData(Cash.getEvents())
        }
    }*/

    private fun initialize() {
        recyclerView = findViewById(R.id.eventList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // если знаем за ранее размер списка то true
        recyclerView.setHasFixedSize(false)
        deleteAll = findViewById(R.id.deleteAll)
    }

    private fun deleteConfirmAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity).apply {
            this.setTitle(R.string.dialogTitle)
                .setMessage(R.string.dialogMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.buttonYes) { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        R.string.descriptionProcessDeletion,
                        Toast.LENGTH_LONG
                    ).show()
                    eventViewModel.deleteAll()
                }
                .setNeutralButton(R.string.buttonCancel) { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        R.string.descriptionProcessActionCanceled,
                        Toast.LENGTH_LONG
                    )
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

            val authPrompt = Class3BiometricAuthPrompt.Builder(
                resources.getString(R.string.titleBiometric),
                resources.getString(R.string.dismissBiometric)
            ).apply {
                setSubtitle(resources.getString(R.string.subtitleBiometric))
                setDescription(resources.getString(R.string.descriptionBiometric))
                setConfirmationRequired(true)
            }.build()

            lifecycleScope.launch {
                try {
                    val authResult =
                        authPrompt.authenticate(AuthPromptHost(this@MainActivity), encryptor)

                    val encryptedEntity = authResult.cryptoObject?.cipher?.let { cipher ->
                        biometricCipher.encrypt("Secret data", cipher)
                    }
                    deleteConfirmAlertDialog()
                    Log.d(MainActivity::class.simpleName, encryptedEntity.toString())
                } catch (e: AuthPromptErrorException) {
                    Log.e("AuthPromptError", e.message ?: "no message")
                } catch (e: AuthPromptFailureException) {
                    Log.e("AuthPromptFailure", e.message ?: "no message")
                }
            }
        }
    }
}
