package com.sample.mycoroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.p1_intro.ThreadVsCoroutineActivity
import com.sample.mycoroutines.p2_suspend.SuspendFunctionsActivity
import com.sample.mycoroutines.p3_builders.CoroutineBuildersActivity
import com.sample.mycoroutines.p5_context_dispatchers.DispatcherDemoActivity
import com.sample.mycoroutines.p4_jobs.JobActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
    }

    private fun initializeViews() {
        main_button_sandbox.setOnClickListener {
            startActivityClass<SandboxActivity>()
        }


        main_button_threads_vs_coroutines.setOnClickListener {
            startActivityClass<ThreadVsCoroutineActivity>()
        }
        main_button_suspend_functions.setOnClickListener {
            startActivityClass<SuspendFunctionsActivity>()
        }
        main_button_coroutine_builders.setOnClickListener {
            startActivityClass<CoroutineBuildersActivity>()
        }
        main_button_jobs_cancel_cr.setOnClickListener {
            startActivityClass<JobActivity>()
        }
        main_button_dispatchers.setOnClickListener {
            startActivityClass<DispatcherDemoActivity>()
        }

    }
}