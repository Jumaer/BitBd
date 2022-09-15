package com.example.bitbd.constant

import android.app.Application
import android.content.Context
import com.example.bitbd.constant.MyApplication

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun getBaseContext(): Context {
        return super.getBaseContext()
    }
}