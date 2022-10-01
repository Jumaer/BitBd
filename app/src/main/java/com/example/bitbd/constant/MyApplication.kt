package com.example.bitbd.constant

import android.app.Application
import android.content.Context
import com.jivosite.sdk.Jivo

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Jivo.init(
            appContext = this,
            widgetId = "y32LR8ttmA"
        )
    }

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun getBaseContext(): Context {
        return super.getBaseContext()
    }
}