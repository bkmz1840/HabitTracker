package com.doubletapp.data.models

import androidx.room.*
import com.doubletapp.domain.models.HabitPriority
import com.doubletapp.domain.models.HabitType
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.util.*

@Entity
data class Habit(
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @TypeConverters(HabitPriorityConverter::class) var priority: HabitPriority,
    @TypeConverters(HabitTypeConverter::class) var type: HabitType,
    @ColumnInfo var countComplete: Int,
    @ColumnInfo(defaultValue = "0") var currentComplete: Int,
    @ColumnInfo var period: Int,
    @ColumnInfo var color: Int,
    @ColumnInfo var uid: String? = null
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}

class HabitPriorityConverter {
    @TypeConverter
    fun toHabitPriority(priorityInt: Int): HabitPriority = when (priorityInt) {
        0 -> HabitPriority.LOW
        1 -> HabitPriority.NEUTRAL
        2 -> HabitPriority.HIGH
        else -> HabitPriority.NONE
    }

    @TypeConverter
    fun fromHabitPriority(habitPriority: HabitPriority): Int = habitPriority.ordinal
}

class HabitTypeConverter {
    @TypeConverter
    fun toHabitType(typeInt: Int): HabitType = when (typeInt) {
        0 -> HabitType.BAD
        1 -> HabitType.GOOD
        else -> HabitType.NONE
    }

    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int = habitType.ordinal
}

class HabitJsonSerializer : JsonSerializer<Habit> {
    // TODO: Serialized name
    override fun serialize(src: Habit, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonObject().apply {
            addProperty("title", src.title)
            addProperty("description", src.description)
            addProperty("color", src.color)
            addProperty("count", src.countComplete)
            addProperty("priority", src.priority.toInt())
            addProperty("frequency", src.period)
            addProperty("date", (Date().time / 1000).toInt())
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
                it.get("done_dates").asJsonArray.size(),
                it.get("frequency").asInt,
                it.get("color").asInt,
                it.get("uid").asString,
            )
        } ?: throw NullPointerException("Json element is null!")
    }
}