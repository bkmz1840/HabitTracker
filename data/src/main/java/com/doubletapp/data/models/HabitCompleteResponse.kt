package com.doubletapp.data.models

data class HabitCompleteResponse(
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = code == null
}