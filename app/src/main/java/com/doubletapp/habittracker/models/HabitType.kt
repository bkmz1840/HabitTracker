package com.doubletapp.habittracker.models

import android.os.Parcelable
import com.doubletapp.habittracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType(val resId: Int): Parcelable {
    NONE(-1),
    BAD(R.string.habit_type_bad),
    GOOD(R.string.habit_type_good);

    fun toDomain(): com.doubletapp.domain.models.HabitType = when (this) {
        BAD -> com.doubletapp.domain.models.HabitType.BAD
        GOOD -> com.doubletapp.domain.models.HabitType.GOOD
        else -> com.doubletapp.domain.models.HabitType.BAD
    }

    companion object {
        fun fromDomain(domainType: com.doubletapp.domain.models.HabitType): HabitType =
            when (domainType) {
                com.doubletapp.domain.models.HabitType.BAD -> BAD
                com.doubletapp.domain.models.HabitType.GOOD -> GOOD
                else -> NONE
            }
    }
}