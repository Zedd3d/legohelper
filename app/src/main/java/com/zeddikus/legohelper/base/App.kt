package com.zeddikus.legohelper.base

import android.app.Application
import com.zeddikus.legohelper.di.AppComponentHolder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppComponentHolder.createComponent(this)
    }
}