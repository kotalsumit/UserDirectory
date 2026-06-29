package com.sumit.userdirectory.feature.users.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val address: AddressDto? = null,
    val company: CompanyDto? = null,
)
