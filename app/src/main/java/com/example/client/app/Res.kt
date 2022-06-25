package com.example.client.app

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Res {
    var context: Context? = null

    @JvmStatic
    fun onStart(context: Context) {
        this.context = context.applicationContext
    }
}