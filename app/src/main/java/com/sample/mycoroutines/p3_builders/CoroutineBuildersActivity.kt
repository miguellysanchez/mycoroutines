package com.sample.mycoroutines.p3_builders

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_cbuilders.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.coroutineContext

class CoroutineBuildersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbuilders)
        initializeViews()
    }

    fun initializeViews() {
        cbuilders_button_runblocking_uidispatcher_coroutine.setOnClickListener {
            runBlockingOnUiThreadDemo()
        }
        cbuilders_button_launch_uidispatcher_coroutine.setOnClickListener {
            println(">>>[LaunchOnUiDispatcher]")
            launchOnUiDispatcherDemo()
        }
        cbuilders_button_launch_commonpool_coroutine.setOnClickListener {
            println(">>>[LaunchOnCommonPoolDispatcher]")
            launchOnCommonPoolDispatcherDemo()
        }
    }

    //    runBlocking blocks the THREAD it is launched from
    fun runBlockingOnUiThreadDemo() {

        runBlocking(UI) {
            println(">>>Starting: runBlocking (${threadName()})")
            Thread.sleep(1000)
            println(">>>Finished: runBlocking (${threadName()})")
        }
        println(">>>Middle outside (${threadName()})")
        run {
            println(">>>Starting: run (${threadName()})")
            Thread.sleep(200)
            println(">>>Finished: run (${threadName()})")
        }
    }

    fun launchOnUiDispatcherDemo() {
        launch(UI) {
            val job = launch(CommonPool) {
                printlnSuspend(">>>Starting: launch (${threadName()})")
                delay(10000)
                printlnSuspend(">>>Finished: launch (${threadName()})")
            }
            delay(1)

//            launch(UI) {
//                printlnSuspend(">>>Starting: launch 2 (${threadName()}) ")
//                delay(1)
//                printlnSuspend(">>>Finished: launch 2 (${threadName()})")
//            }

            println(">>>Middle outside (${threadName()})")
            run {
                println(">>>Starting: run (${threadName()})")
                delay(2000)
                job.join()
                println(">>>Finished: run (${threadName()})")
            }
        }
    }

    fun launchOnCommonPoolDispatcherDemo() {
        launch(CommonPool) {
            println(">>>Starting: launch (${threadName()}) ")
            Thread.sleep(1000)
            println(">>>Finished: launch (${threadName()})")
        }
        println(">>>Middle outside (${threadName()})")
        run {
            println(">>>Starting: run (${threadName()})")
            Thread.sleep(200)
            println(">>>Finished: run (${threadName()})")
        }
    }


    suspend fun printlnSuspend(s: String) {
        println(s)
    }


    suspend fun something() {
        val rootParent = launch {

        }
//        Builders
        val anExecutor: ExecutorService = Executors.newFixedThreadPool(100)

        val asyncTaskExecutor = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
        launch(anExecutor.asCoroutineDispatcher()) {

        }


        launch(parent = rootParent) {

        }

        run {

        }
        runBlocking {
            withContext(start = CoroutineStart.DEFAULT, context = CommonPool) {

            }
        }

//      Deferred
        val o: Deferred<String> = async {
            ""
        }
        o.await()
//        suspendCoroutine<> {  }

        val aDefeerredCoroutine = async() {
            220
        }
    }

    suspend fun aSuspendFun() {
        coroutineContext // accesses the current coroutine context within a suspend function
    }

    fun threadName() = Thread.currentThread().name

}