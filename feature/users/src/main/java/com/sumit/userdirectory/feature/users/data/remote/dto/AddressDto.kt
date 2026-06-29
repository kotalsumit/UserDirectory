package com.sumit.userdirectory.feature.users.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val street: String? = null,
    val suite: String? = null,
    val city: String? = null,
    val zipcode: String? = null,
)
