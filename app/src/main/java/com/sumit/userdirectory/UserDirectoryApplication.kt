package com.sumit.userdirectory

import android.app.Application
import com.sumit.userdirectory.core.common.di.commonModule
import com.sumit.userdirectory.core.network.di.networkModule
import com.sumit.userdirectory.feature.users.di.usersFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UserDirectoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UserDirectoryApplication)
            modules(
                commonModule,
                networkModule,
                usersFeatureModule,
            )
        }
    }
}
