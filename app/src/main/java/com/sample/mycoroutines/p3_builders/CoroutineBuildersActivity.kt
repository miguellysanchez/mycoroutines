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
                val int1 = deferredInt1.await()
                val int2 = deferredInt2.await()
                println("The sum of $int1 and $int2 = ${int1 + int2}")
                println("Async operation completed in ${System.currentTimeMillis() - startTime} ms")

        }
    }

    private fun runBlockingDemo() {
        runBlocking {
            //runBlocking should not be called on Android UI thread
            // will cause : "java.lang.IllegalStateException:
            // runBlocking is not allowed in Android main looper thread"
            println("Do something first, but instead crash")
        }
        println("Should print after runBlocking, but is not printed")
    }


    private fun runBlockingProperlyDemo() {
        launch{
            val num = runBlocking<Int> {
                println("1 - First")
                delay(3000)
                println("2 - Second even after delay, code outside prints after runBlocking finishes")
                42
            }
            println("3 - Last after runBlocking: $num")//prints 42
        }
        println("0 - Outside the enclosing launch")
    }

    private fun threadName() = Thread.currentThread().name


}