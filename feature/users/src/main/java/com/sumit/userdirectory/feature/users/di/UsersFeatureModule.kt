package com.sumit.userdirectory.feature.users.di

import com.sumit.userdirectory.feature.users.data.remote.KtorUsersRemoteDataSource
import com.sumit.userdirectory.feature.users.data.remote.UsersRemoteDataSource
import com.sumit.userdirectory.feature.users.data.repository.UsersRepositoryImpl
import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository
import com.sumit.userdirectory.feature.users.domain.usecase.GetUserDetailUseCase
import com.sumit.userdirectory.feature.users.domain.usecase.GetUsersUseCase
import com.sumit.userdirectory.feature.users.presentation.detail.UserDetailViewModel
import com.sumit.userdirectory.feature.users.presentation.list.UserListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val usersFeatureModule = module {
    single<UsersRemoteDataSource> { KtorUsersRemoteDataSource(httpClient = get()) }
    single<UsersRepository> {
        UsersRepositoryImpl(
            remoteDataSource = get(),
            dispatcherProvider = get(),
        )
    }
    factory { GetUsersUseCase(usersRepository = get()) }
    factory { GetUserDetailUseCase(usersRepository = get()) }
    viewModel { UserListViewModel(getUsersUseCase = get()) }
    viewModel { UserDetailViewModel(getUserDetailUseCase = get()) }
}
