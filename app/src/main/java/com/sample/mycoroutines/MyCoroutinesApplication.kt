package com.sample.mycoroutines

import android.app.Application
import timber.log.Timber

class MyCoroutinesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}