package com.sample.mycoroutines.p3_builders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.R
import com.sample.mycoroutines.RandomOperations
import kotlinx.android.synthetic.main.activity_cbuilders.*
import kotlinx.coroutines.experimental.*
import java.util.*

class CoroutineBuildersActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbuilders)
        initializeViews()
    }

    private fun initializeViews() {
        cbuilders_button_launch_demo.setOnClickListener { launchDemo() }
        cbuilders_button_launch_error_demo.setOnClickListener { launchWithErrorDemo() }
        cbuilders_button_async_demo.setOnClickListener { asyncDemo() }
        cbuilders_button_async_error_demo.setOnClickListener { asyncWithErrorDemo() }
        cbuilders_button_run_blocking_demo.setOnClickListener { runBlockingDemo() }
        cbuilders_button_run_blocking_properly_demo.setOnClickListener { runBlockingProperlyDemo() }
    }


    private fun launchDemo() {
        launch {
            val job = launch {
                println("->World!!")
                delay(1000)
                println("-->Hello World")
                delay(2000)
            }
            println("Hello,")
            job.join()
            println("--->Finished!")
        }
    }

    private fun launchWithErrorDemo() {
        launch {
            println("->World!!")
            delay(1000)
            println("---> Hello World")
            throw IllegalStateException()
        }
        println("Hello,")
    }

    private suspend fun getDelayedInt1(): Int {
        delay(1000)
        return Random().nextInt(100)
    }

    private suspend fun getDelayedInt2(): Int {
        delay(1000)
        return Random().nextInt(100)
    }

    private fun asyncDemo() {
        val startTime = System.currentTimeMillis()
        val deferredInt1: Deferred<Int> = async {
            getDelayedInt1()
        }
        val deferredInt2: Deferred<Int> = async {
            getDelayedInt2()
        }
        async {
            println("Starting async operation")
            val int1 = deferredInt1.await()
            val int2 = deferredInt2.await()
            println("The sum of $int1 and $int2 = ${int1 + int2}")
            println("Async operation completed in ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private fun asyncWithErrorDemo() {
        val startTime = System.currentTimeMillis()
        val deferredInt1: Deferred<Int> = async {
            getDelayedInt1()
        }
        val deferredInt2: Deferred<Int> = async {
            getDelayedInt2()
            throw IllegalStateException("Caught exception in the async")
        }
        async {
            println("Starting async operation")
            try {
                val int1 = deferredInt1.await()
                val int2 = deferredInt2.await()
                println("The sum of $int1 and $int2 = ${int1 + int2}")
                println("Async operation completed in ${System.currentTimeMillis() - startTime} ms")
            } catch (e: Exception) {
                println("Caught exception : $e")
                e.printStackTrace()
            }
        }
    }

    private fun runBlockingDemo() {
        runBlocking() {

        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun runBlockingProperlyDemo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun threadName() = Thread.currentThread().name


}