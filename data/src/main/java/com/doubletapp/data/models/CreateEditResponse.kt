package com.doubletapp.data.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class CreateEditResponse(
    val uid: String? = null,
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = uid != null
}

class CreateEditResponseJsonDeserializer: JsonDeserializer<CreateEditResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CreateEditResponse {
        val jsonObj = json?.asJsonObject
        jsonObj?.let {
            if (it.has("uid")) return CreateEditResponse(it.get("uid").asString)
            return CreateEditResponse(
                code = it.get("code").asInt,
                message = it.get("message").asString
            )
        } ?: throw NullPointerException("Json element is null!")
    }
}
