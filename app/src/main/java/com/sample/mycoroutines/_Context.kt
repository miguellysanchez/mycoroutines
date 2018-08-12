package com.sample.mycoroutines

import android.content.Context
import android.content.Intent

inline fun <reified T: Context> Context.startActivityClass(noinline intentModifiers : Intent.() -> Unit = {}){
    val intent = Intent(this, T::class.java).apply(intentModifiers)
    startActivity(intent)
}