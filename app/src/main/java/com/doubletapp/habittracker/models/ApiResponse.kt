package com.doubletapp.habittracker.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class ApiResponse(
    val uid: String? = null,
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = uid != null
}

class ApiResponseJsonDeserializer: JsonDeserializer<ApiResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ApiResponse {
        val jsonObj = json?.asJsonObject
        jsonObj?.let {
            if (it.has("uid")) return ApiResponse(it.get("uid").asString)
            return ApiResponse(
                code = it.get("code").asInt,
                message = it.get("message").asString
            )
        } ?: throw NullPointerException("Json element is null!")
    }
}