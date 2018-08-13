package com.sample.mycoroutines.p5_context_dispatchers

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_dispatchers.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.ContinuationInterceptor
import kotlin.coroutines.experimental.coroutineContext

class DispatcherDemoActivity : AppCompatActivity() {

    val random = Random()
    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatchers)
        initializeViews()
    }

    fun initializeViews() {
        dialog = ProgressDialog(this)
        dispatchers_button_run_ui_dispatcher.setOnClickListener {
            println(">>>[LaunchOnUiDispatcher]")
            launchOnUiDispatcherDemo()
        }
        dispatchers_button_run_commonpool_dispatcher.setOnClickListener {
            println(">>>[LaunchOnCommonPoolDispatcher]")

            launchOnCommonPoolDispatcherDemo()
        }
        dispatchers_button_run_unconfined_dispatcher.setOnClickListener {
            launchOnUnconfinedDispatcherDemo()
        }
        dispatchers_button_run_nstc_dispatcher.setOnClickListener {
            launchOnNewSingleThreadContextDemo()
        }
        dispatchers_button_run_nftpc_dispatcher.setOnClickListener {
            launchOnNewFixedThreadPoolDemo()
        }
        dispatchers_button_run_with_context.setOnClickListener {
            withContextDemo()
        }

    }


    fun launchOnUiDispatcherDemo() {
        //Contains the UI dispatcher with UUID for its name
        val cName = "UI_" + generateRandomName()
        launch(UI + CoroutineName(cName)) {
            var i = 0
            while (i < 10) {
                i++
                println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                //can run view rendering here since it is inside main thread
                dispatchers_textview_indicator.text = "${coroutineContext[CoroutineName]} -> ($i)"
                delay(500)
            }
        }
    }

    fun launchOnCommonPoolDispatcherDemo() {
        val cName = "CommonPool_" + generateRandomName()
        launch(CommonPool + CoroutineName(cName)) {
            var i = 0
            while (i < 20) {
                i++
                println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                delay(500)
            }
        }
    }

    fun launchOnUnconfinedDispatcherDemo() {
        val cName = "Unconfined_" + generateRandomName()
        launch(Unconfined + CoroutineName(cName)) {
            var i = 0
            while (i < 15) {
                i++
                println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                delay(500)
            }
        }
    }

    fun launchOnNewSingleThreadContextDemo() {
        val cName = "NewSingleThreadContext_" + generateRandomName()
        val genThreadName = "STC_${generateRandomName(4)}"
        val stc = newSingleThreadContext(genThreadName)
        val job = launch(stc + CoroutineName(cName)) {
            var i = 0
            while (i < 15) {
                i++
                println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                delay(500)
            }
        }
        launch(UI) {
            job.join()
            println("Closing thread @$genThreadName")
            stc.close()
        }
    }

    fun launchOnNewFixedThreadPoolDemo() {
        val cName = "NewFixedThreadPool_" + generateRandomName()
        val genThreadName = "STC_${generateRandomName(4)}"
        val stc = newFixedThreadPoolContext(4, genThreadName)
        val job = launch(stc + CoroutineName(cName)) {
            var i = 0
            while (i < 20) {
                i++
                println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                delay(500)
            }
        }
        launch(UI) {
            job.join()
            println("Closing thread @$genThreadName")
            stc.close()
        }

    }

    fun withContextDemo() {
        launch {
            var randomNum = 0
            var i = 0
            var dispatcher: CoroutineDispatcher = UI
            val stc = newSingleThreadContext("STC-01")
            val ftpc = newFixedThreadPoolContext(3, "FTPC")
            while (i < 20) {
                i++
                dispatcher  = when (randomNum % 4) {
                    0 -> UI
                    1 -> CommonPool
                    2 ->  stc
                    else -> ftpc
                }
                println(">>>Using dispatcher: ${dispatcher[ContinuationInterceptor]}")
                randomNum = withContext(dispatcher){
                    println("Running i[$i] CName[${coroutineContext[CoroutineName]}] w/ Dispatcher[${coroutineContext[ContinuationInterceptor]}] @${threadName()}]")
                    delay(500)
                    random.nextInt()
                }
                println(">> Number generated: $randomNum")
            }
        }
    }

    suspend fun extraStuff() {
        val anExecutor: ExecutorService = Executors.newFixedThreadPool(4)
        val asyncTaskExecDispatcher = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
        launch(anExecutor.asCoroutineDispatcher()) {
            delay(100)
        }
        launch(asyncTaskExecDispatcher){
            delay(100)
        }
    }

    suspend fun aSuspendFun() {
        coroutineContext // accesses the current coroutine context within a suspend function
    }

    private fun threadName() = Thread.currentThread().name

    private fun generateRandomName(length: Int = 31): String {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var s = ""
        for (i in 0..length) {
            s += chars[Math.floor(Math.random() * chars.length).toInt()]
        }
        return s
    }

}