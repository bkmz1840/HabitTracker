package com.doubletapp.data.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class HabitCompleteResponse(
    val code: Int? = null,
    val message: String? = null
) {
    val isSuccess: Boolean = code == null
}

class HabitCompleteResponseJsonDeserializer: JsonDeserializer<HabitCompleteResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HabitCompleteResponse {
        val jsonObj = json?.asJsonObject
        jsonObj?.let {
            if (it.has("code")) return HabitCompleteResponse(
                code = it.get("code").asInt,
                message = it.get("message").asString
            )
            return HabitCompleteResponse()
        } ?: throw NullPointerException("Json element is null!")
    }
}