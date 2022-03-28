package com.example.mystartkotlin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.mystartkotlin.R

class ProgressFragment : Fragment() {

    lateinit var threadProgress: Thread

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val view: View = inflater.inflate(R.layout.progress_fragment, container, false)
        @Suppress("unused")
        val indicatorBar: ProgressBar = view.findViewById(R.id.indicator)

        threadProgress = Thread(
            kotlinx.coroutines.Runnable {
            /*    for (i in 1..100) {
                    try {
                        Log.e("nm", " $i")
                        indicatorBar.setProgress(i)
                        Thread.sleep(200)
                    } catch (e: InterruptedException) {
                        Log.e(e.toString(), "!!!!!!!!!!!!!!!!!!!")
                        break
                    }
                }*/
            }
        )
        threadProgress.start()

        return view
    }
}
