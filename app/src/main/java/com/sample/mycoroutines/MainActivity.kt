package com.sample.mycoroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sample.mycoroutines.p1_intro.ThreadVsCoroutineActivity
import com.sample.mycoroutines.p2_builders.CoroutineBuildersActivity
import com.sample.mycoroutines.p3_cancel.CancelCoroutineActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
    }

    private fun initializeViews() {
        main_button_threads_vs_coroutines.setOnClickListener{
            startActivityClass<ThreadVsCoroutineActivity>()
        }

        main_button_coroutine_builders.setOnClickListener{
            startActivityClass<CoroutineBuildersActivity>()
        }

        main_button_sandbox.setOnClickListener {
            startActivityClass<SandboxActivity>()
        }

        main_button_cancel_cr.setOnClickListener{
            startActivityClass<CancelCoroutineActivity>()
        }


    }
}