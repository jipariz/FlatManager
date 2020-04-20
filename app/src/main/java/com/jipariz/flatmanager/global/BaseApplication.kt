package com.jipariz.flatmanager.global

import android.app.Application
import com.jipariz.flatmanager.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            // declare modules
            modules(appModule)
        }
    }
}