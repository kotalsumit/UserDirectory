package com.sumit.userdirectory.feature.users.domain.model

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val fullAddress: String,
)
