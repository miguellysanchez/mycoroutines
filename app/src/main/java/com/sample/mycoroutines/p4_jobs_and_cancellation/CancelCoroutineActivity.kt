package com.sample.mycoroutines.p4_jobs_and_cancellation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_cancel_cr.*
import kotlinx.coroutines.experimental.*
import java.util.*
import kotlin.coroutines.experimental.coroutineContext

class CancelCoroutineActivity : AppCompatActivity() {

    var jobList: MutableList<Job> = mutableListOf()
    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_cr)
        initializeViews()
    }

    fun initializeViews() {
        cancel_cr_button_launch_job.setOnClickListener {
            val jobName = UUID.randomUUID().toString()
            println(">>>Starting a new job with jobid: $jobName")
            val longRunningJob = launch(CommonPool + CoroutineName(jobName)) {
                while (true) {
                    doWorkA()
                    doWorkB()
                    delay(2000)
                }
            }
//            val disposable = longRunningJob.invokeOnCompletion() {
//                println(">>>> Invoked onCompletion")
//
//            }
//            disposable.dispose()

            jobList.add(longRunningJob)
        }

        cancel_cr_button_cancel_job.setOnClickListener {
            if (!jobList.isEmpty()) {
                val a = launch {
                    val toCancelJob = jobList.removeAt(0)
                    println(">>>Starting cancellation of job (${toCancelJob[CoroutineName]}) at thread(${threadName()})")
                    toCancelJob.cancel()
                    toCancelJob.join()
                }
                println(">>>Finished cancel and join")
            } else {
                Toast.makeText(this, "No jobs to cancel!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun aSuspendFunction() {
        yield()
    }

    suspend fun doWorkA() {
        println("OOOOOOOOOOOO Starting work ${coroutineContext[CoroutineName]} at thread: ${threadName()}")
        delay(random.nextInt(2000))
        println("OOOOOOOOOOOO Finished work ${coroutineContext[CoroutineName]} at thread: ${threadName()}")
    }

    suspend fun doWorkB() {
        println("XXXXXXXXXXXXX Starting work ${coroutineContext[CoroutineName]} at thread: ${threadName()}")
        delay(random.nextInt(2000))
        println("XXXXXXXXXXXXX Worked ${coroutineContext[CoroutineName]} at thread: ${threadName()}")
    }

    fun threadName() = Thread.currentThread().name
}



