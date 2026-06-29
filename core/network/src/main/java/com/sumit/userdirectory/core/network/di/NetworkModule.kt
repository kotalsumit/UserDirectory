package com.sumit.userdirectory.core.network.di

import com.sumit.userdirectory.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { HttpClientFactory.create() }
}
