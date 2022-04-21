package com.doubletapp.habittracker.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IHabitDao {
    @Query("SELECT * FROM habit WHERE title LIKE :title || '%'")
    fun getAll(title: String = ""): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id LIMIT 1")
    fun findHabitById(id: Int): LiveData<Habit>

    @Insert
    fun insert(vararg habit: Habit)

    @Update
    fun update(vararg habit: Habit)
}