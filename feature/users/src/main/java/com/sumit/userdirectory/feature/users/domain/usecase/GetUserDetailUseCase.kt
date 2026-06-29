package com.sumit.userdirectory.feature.users.domain.usecase

import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository

class GetUserDetailUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(userId: Int) = usersRepository.getUserById(userId)
}
