package com.sample.mycoroutines.p4_jobs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_jobs.*
import kotlinx.coroutines.experimental.*
import java.util.*
import kotlin.coroutines.experimental.coroutineContext

class JobActivity : AppCompatActivity() {

    var lazyJob: Job? = null

    var jobList: MutableList<Job> = mutableListOf()
    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)
        initializeViews()

    }

    fun initializeViews() {

        jobs_button_create_lazy_job.setOnClickListener {
            createLazyJob()
        }
        jobs_button_start_lazy_job.setOnClickListener {
            startLazyJob()
        }

        jobs_button_launch_no_coop_cancel_job.setOnClickListener {
            launchJobWithNoCoopCancel()
        }
        jobs_button_launch_job_with_is_active.setOnClickListener {
            launchCancellableJobCheckActive()
        }
        jobs_button_launch_job_with_delay.setOnClickListener {
            launchCancellableJobWithDelay()
        }

        jobs_button_cancel_job.setOnClickListener {
            cancelAJob()
        }

        jobs_button_timeout_job.setOnClickListener {
            launchWithTimeout()
        }
    }

    private fun createLazyJob() {
        lazyJob = launch(start = CoroutineStart.LAZY) {
            println(">>>Starting the lazy job")
            delay(500)
            println(">>>Finished the lazy job")
        }
        println(">>>Created a lazy job")
    }

    private fun startLazyJob() {
        if (lazyJob != null && !lazyJob!!.isActive) {
            lazyJob!!.start()
        }
    }

    fun launchJobWithNoCoopCancel() {
        val jobName = UUID.randomUUID().toString()
        println(">>>Starting a new job with jobid: $jobName")
        var nextWorkTime = System.currentTimeMillis()
        var i = 0
        val longRunningJob = launch/*(CommonPool + CoroutineName(jobName)) */{
            while (i < 20) {
                if (System.currentTimeMillis() >= nextWorkTime) {
                    i++
                    doWorkNonCancellable(i)
                    nextWorkTime += 500L
                }
            }
        }
        jobList.add(longRunningJob)
    }

    fun launchCancellableJobCheckActive() {
        val jobName = UUID.randomUUID().toString()
        println(">>>Starting a new job with jobid: $jobName")
        var nextWorkTime = System.currentTimeMillis()
        var i = 0
        val longRunningJob = launch(CommonPool + CoroutineName(jobName)) {
            while (isActive) {
                if (System.currentTimeMillis() >= nextWorkTime) {
                    i++
                    doWorkNonCancellable(i)
                    nextWorkTime += 500L
                }
            }
        }
        jobList.add(longRunningJob)
    }

    fun launchCancellableJobWithDelay() {
        val jobName = UUID.randomUUID().toString()
        println(">>>Starting a new job with jobid: $jobName")
        var nextWorkTime = System.currentTimeMillis()
        var i = 0
        val longRunningJob = launch {
            while (i < 20) {
                if (System.currentTimeMillis() >= nextWorkTime) {
                    i++
                    doWorkNonCancellable(i)
                    doWorkCancellable(i)
                    nextWorkTime += 500L
                }
            }
        }
        jobList.add(longRunningJob)
    }


    fun cancelAJob() {
        if (!jobList.isEmpty()) {
            launch {
                val toCancelJob = jobList.removeAt(0)
                println(">>>Cancellation of job")
                toCancelJob.cancel()
                toCancelJob.join()
            }
            println(">>>Finished cancel and join")
        } else {
            Toast.makeText(this, "No jobs to cancel!", Toast.LENGTH_SHORT).show()
        }
    }


    suspend fun doWorkNonCancellable(i: Int) {
        println("NON_CANCEL Starting work $i ")
        println("NON_CANCEL Finished work $i ")
    }

    suspend fun doWorkCancellable(i: Int) {
        println("CANCELLABLE Starting work $i ")
        delay(random.nextInt(2000))
        println("CANCELLABLE Worked $i ")
    }

    fun launchWithTimeout() {
        launch(CommonPool) {
            val numIterations = jobs_edittext_timeout.text.toString().toInt().coerceAtLeast(1).coerceAtMost(20000)
            println(">>>Starting timeout")
            var i = 0
            try {
                withTimeout(5000) {
                    while (i < numIterations) {
                        i++
                        println("Running for iteration $i")
                        delay(100)
                    }
                }
                println(">>>Job has completed")

            } catch (e: TimeoutCancellationException) {
                println("!!!TimeoutCancellationException: $e")
            }
        }
    }


    fun basicCoroutineCancel() {
        val job = launch {
            //use this flag to enable cancellation
            while (isActive) {
                //...DO WORK
                //or use delay/yield
                delay(100)
            }
        }
        job.cancel()
        // OR
        job.cancel(CancellationException("Reason"))
    }

//    fun threadName() = Thread.currentThread().name

}



