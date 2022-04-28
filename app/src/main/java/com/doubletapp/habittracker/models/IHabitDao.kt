package com.doubletapp.habittracker.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IHabitDao {
    @Query("SELECT * FROM habit WHERE title LIKE :title || '%'")
    fun getAll(title: String = ""): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id LIMIT 1")
    fun findHabitById(id: Int): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg habit: Habit)

    @Update
    suspend fun update(vararg habit: Habit)

    @Query("DELETE FROM habit")
    suspend fun deleteAll()
}