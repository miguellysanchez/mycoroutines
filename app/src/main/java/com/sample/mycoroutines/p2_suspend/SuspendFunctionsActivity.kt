package com.sample.mycoroutines.p2_suspend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_suspend_function.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.yield
import java.util.*
import kotlin.coroutines.experimental.suspendCoroutine

class SuspendFunctionsActivity : AppCompatActivity() {

    private val random = Random()
    private var raceEnd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suspend_function)
        suspend_function_button_race.setOnClickListener {
            suspend_function_button_race.visibility = View.GONE
            startUpdate()
        }
    }

    private fun startUpdate() {
        raceEnd = false
        launch(UI) { startRunning(suspend_function_textview_red) }
        launch(UI) { startRunning(suspend_function_textview_green) }
        launch(UI) { startRunning(suspend_function_textview_blue) }
    }


    suspend fun startRunning(textView: TextView) {
        textView.text = "0"
        var progress = textView.text.toString().toInt()
        while (progress < 1000 && !raceEnd) {
            println(">>>Updating: ${textView.tag}")
//            delay(10)
            yield()
            println(">>>After yield: ${textView.tag}")
            progress += 1 + random.nextInt(20)
            updateTextView(textView, progress)
        }
        if (!raceEnd) {
            raceEnd = true
            Toast.makeText(this, "${textView.tag} won!", Toast.LENGTH_SHORT).show()
            suspend_function_button_race.visibility = View.VISIBLE
        }
    }

    suspend fun updateTextView(textView: TextView, progress: Int) {
        textView.text = progress.toString()
        suspendCoroutine<Unit> {  }
    }
}