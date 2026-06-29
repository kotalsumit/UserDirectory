package com.sumit.userdirectory.feature.users.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(
    val name: String? = null,
    val catchPhrase: String? = null,
    val bs: String? = null,
)
