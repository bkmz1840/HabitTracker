package com.doubletapp.domain.models

data class ApiResponse(
    val uid: String? = null,
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = uid != null
}