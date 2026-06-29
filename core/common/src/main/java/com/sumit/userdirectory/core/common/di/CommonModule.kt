package com.sumit.userdirectory.core.common.di

import com.sumit.userdirectory.core.common.coroutine.DefaultDispatcherProvider
import com.sumit.userdirectory.core.common.coroutine.DispatcherProvider
import org.koin.dsl.module

val commonModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}
