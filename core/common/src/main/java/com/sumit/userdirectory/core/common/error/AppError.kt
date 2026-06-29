package com.sumit.userdirectory.core.common.error

sealed interface AppError {
    data class Network(val message: String) : AppError
    data class NotFound(val message: String) : AppError
    data class Unknown(val message: String) : AppError
}
