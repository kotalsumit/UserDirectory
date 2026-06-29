package com.sumit.userdirectory.feature.users.domain.usecase

import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository

class GetUsersUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke() = usersRepository.getUsers()
}
