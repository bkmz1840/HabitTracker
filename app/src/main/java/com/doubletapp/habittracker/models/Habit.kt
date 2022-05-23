package com.doubletapp.habittracker.models

import androidx.room.*
import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

@Entity
data class Habit(
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @TypeConverters(HabitPriorityConverter::class) var priority: HabitPriority,
    @TypeConverters(HabitTypeConverter::class) var type: HabitType,
    @ColumnInfo var countComplete: Int,
    @ColumnInfo var period: Int,
    @ColumnInfo var color: Int,
    @ColumnInfo var uid: String? = null
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}

class HabitJsonSerializer : JsonSerializer<Habit> {
    override fun serialize(src: Habit, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonObject().apply {
            addProperty("title", src.title)
            addProperty("description", src.description)
            addProperty("color", -1 * src.color)
            addProperty("count", src.countComplete)
            addProperty("priority", src.priority.toInt())
            addProperty("frequency", src.period)
            addProperty("date", Date().time)
            addProperty("type", src.type.toInt())
            if (src.uid != null)
                addProperty("uid", src.uid)
        }
}

class HabitJsonDeserializer: JsonDeserializer<Habit> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Habit {
        val jsonObj = json?.asJsonObject
        return jsonObj?.let {
            Habit(
                it.get("title").asString,
                it.get("description").asString,
                HabitPriority.fromInt(it.get("priority").asInt),
                HabitType.fromInt(it.get("type").asInt),
                it.get("count").asInt,
                it.get("frequency").asInt,
                -1 * it.get("color").asInt,
                it.get("uid").asString,
            )
        } ?: throw NullPointerException("Json element is null!")
    }
}