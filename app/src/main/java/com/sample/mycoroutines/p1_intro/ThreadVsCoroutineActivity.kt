package com.sample.mycoroutines.p1_intro

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_tvsc.*
import kotlinx.coroutines.experimental.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class ThreadVsCoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvsc)
        initializeViews()
    }

    private fun initializeViews() {
        tvsc_button_spawn_threads.setOnClickListener {
            val n = tvsc_edittext_count.text.toString().toIntOrNull()
            val timeElapsed = measureTimeMillis {
                startNThreads(n)
            }
            println(">>>Finished spawning $n threads in : $timeElapsed ms")
        }
        tvsc_button_spawn_coroutines.setOnClickListener {
            val n = tvsc_edittext_count.text.toString().toIntOrNull()
            val timeElapsed = measureTimeMillis {
                startNCoroutines(n)
            }
            println(">>>Finished spawning $n coroutines in : $timeElapsed ms")
        }
    }


    private fun startNThreads(n: Int?) {
        val numThreads = n ?: 0
        val jobs = List(numThreads) {
            Thread {
                Thread.sleep(1000)
                print(",")
            }
        }
        jobs.forEach{it.start()}
        jobs.forEach<Thread> { it.join() }

    }


    private fun startNCoroutines(n: Int?) = launch {
        val numCoroutines = n ?: 0
        val jobs = List(numCoroutines) {
            launch {
                delay(1000)
                print(".")
            }
        }
        jobs.forEach<Job> { it.join()}
    }

}