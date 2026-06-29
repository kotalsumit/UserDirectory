package com.sumit.userdirectory

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UserDirectoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UserDirectoryApplication)
            modules(emptyList())
        }
    }
}
