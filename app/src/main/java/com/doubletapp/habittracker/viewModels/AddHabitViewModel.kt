package com.doubletapp.habittracker.viewModels

import android.graphics.Color
import androidx.lifecycle.*
import com.doubletapp.domain.HabitsInteractor
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitPriority
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.fromDomain
import com.doubletapp.habittracker.util.toDomain
import com.doubletapp.habittracker.util.toMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddHabitViewModel(
    habitId: Int?,
    private val interactor: HabitsInteractor,
) : ViewModel() {
    val habit: MutableLiveData<Habit> = habitId?.let {
        liveData { emit(interactor.findHabitById(it).fromDomain()) }.toMutableLiveData()
    } ?: MutableLiveData<Habit>()
    var habitType = HabitType.NONE
    var habitColor = Color.WHITE
    var habitPriority = HabitPriority.NONE
    var habitPeriod: Long = -1L
    val validationErrors: MutableLiveData<List<String>> = MutableLiveData()

    private val _progressLoad: MutableLiveData<Boolean> = MutableLiveData()
    val progressLoad: LiveData<Boolean> = _progressLoad

    fun validateHabit(
        title: String,
        description: String,
        countComplete: Int?
    ) {
        _progressLoad.postValue(true)
        val errorFields = mutableListOf<String>()
        if (title.isEmpty()) errorFields.add(Settings.ERROR_FIELD_TITLE)
        if (description.isEmpty()) errorFields.add(Settings.ERROR_FIELD_DESCRIPTION)
        if (habitPriority == HabitPriority.NONE) errorFields.add(Settings.ERROR_FIELD_PRIORITY)
        if (countComplete == null) errorFields.add(Settings.ERROR_FIELD_COUNT_COMPLETE)
        if (habitPeriod == -1L) errorFields.add(Settings.ERROR_FIELD_PERIOD)
        if (habitType == HabitType.NONE) errorFields.add(Settings.ERROR_FIELD_TYPE)

        if (errorFields.isNotEmpty()) {
            validationErrors.postValue(errorFields)
            _progressLoad.postValue(false)
        }
        else {
            viewModelScope.launch(Dispatchers.IO) {
                if (countComplete != null) viewModelScope.launch(Dispatchers.IO) {
                    uploadHabit(title, description, countComplete)
                    validationErrors.postValue(listOf())
                }
                _progressLoad.postValue(false)
            }
        }
    }

    private suspend fun uploadHabit(
        title: String,
        description: String,
        countComplete: Int
    ) {
        habit.value?.let {
            it.title = title
            it.description = description
            it.priority = habitPriority
            it.type = habitType
            it.countComplete = countComplete
            it.period = habitPeriod
            it.color = habitColor
            withContext(Dispatchers.IO) {
                interactor.insertUpdate(it.toDomain())
            }
        } ?: run {
            withContext(Dispatchers.IO) {
                interactor.insertUpdate(
                    Habit(
                        null,
                        title,
                        description,
                        habitPriority,
                        habitType,
                        countComplete,
                        0,
                        habitPeriod,
                        habitColor
                    ).toDomain()
                )
            }
        }
    }
}