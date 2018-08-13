package com.sample.mycoroutines.p2_suspend

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_suspend_function.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.*
import kotlin.coroutines.experimental.coroutineContext

class SuspendFunctionsActivity : AppCompatActivity() {

    private var leftJob: Job? = null
    private var rightJob: Job? = null


    private val random = Random()
    private var raceEnd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suspend_function)
        initializeViews()
    }

    private fun initializeViews() {
        suspend_function_button_consecutive_fxn_start.setOnClickListener {
            suspend_function_button_consecutive_fxn_start.visibility = View.GONE
            //Try increasing the delay value of one job and decreasing the other
            leftJob = async(UI) {
                while (true) {
                    clearBg(suspend_function_textview_right, 10)
                    colorBg(suspend_function_textview_left, 10)
                }
            }
            rightJob = async(UI) {
                while (true) {
                    clearBg(suspend_function_textview_left, 100)
                    colorBg(suspend_function_textview_right, 100)
                }
            }
            suspend_function_button_consecutive_fxn_stop.visibility = View.VISIBLE
        }

        suspend_function_button_consecutive_fxn_stop.setOnClickListener {
            suspend_function_button_consecutive_fxn_stop.visibility = View.GONE
            leftJob?.cancel()
            rightJob?.cancel()
            leftJob = null
            rightJob = null
            suspend_function_button_consecutive_fxn_start.visibility = View.VISIBLE
        }

        suspend_function_button_race.setOnClickListener {
            suspend_function_button_race.visibility = View.GONE
            startRace()
        }
    }

    suspend fun clearBg(textView: TextView, delayValue: Int) {
        println(">>>Clearing ${textView.text}")
        textView.setBackgroundColor(Color.TRANSPARENT)
        delay(delayValue.coerceAtLeast(2))
    }

    suspend fun colorBg(textView: TextView, delayValue: Int) {
        println(">>>Coloring ${textView.text}")
        textView.setBackgroundColor(Color.GREEN)
        delay(delayValue.coerceAtLeast(2))
    }



    private fun startRace() {
        raceEnd = false
        launch(UI) { startRacing(suspend_function_textview_red) }
        launch(UI) { startRacing(suspend_function_textview_green) }
        launch(UI) { startRacing(suspend_function_textview_blue) }
    }


    suspend fun startRacing(textView: TextView) {
        textView.text = "0"
        var progress = textView.text.toString().toInt()
        while (progress < 1000 && !raceEnd) {
            delay(10)
            progress += 1 + random.nextInt(20)
            updateTextView(textView, progress)
            println("Using coroutine ctx with job ${coroutineContext[Job]}")
        }
        if (!raceEnd) {
            raceEnd = true
            Toast.makeText(this, "${textView.tag} won!",
                    Toast.LENGTH_SHORT).show()
            suspend_function_button_race.visibility = View.VISIBLE
        }
    }

    suspend fun updateTextView(textView: TextView, progress: Int) {
        textView.text = progress.toString()
    }
}