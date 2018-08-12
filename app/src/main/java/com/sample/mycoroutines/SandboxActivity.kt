package com.sample.mycoroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sandbox.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking


class SandboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sandbox)
        initializeViews()
    }

    private fun initializeViews() {
        sandbox_button_launch.setOnClickListener {
            val aLaunchJob: Job = launch() {
                println(">>>>Starting the launch coroutine builder")
                runBlocking {
                    delay(2000)
                }
                println(">>>Done with the launch coroutine")
                //                Toast.makeText(this@SandboxActivity, "Done with the launch", Toast.LENGTH_SHORT).show()
            }
            println(">>After the delay")
        }
        runOnUiThread{

        }

    }
}