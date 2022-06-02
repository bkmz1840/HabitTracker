package com.doubletapp.data.models

data class CreateEditResponse(
    val uid: String? = null,
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = uid != null
}
