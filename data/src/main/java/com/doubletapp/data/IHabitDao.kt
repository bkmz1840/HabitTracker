package com.doubletapp.data

import androidx.room.*
import com.doubletapp.data.models.Habit

@Dao
interface IHabitDao {
    @Query("SELECT * FROM habit WHERE title LIKE :title || '%'")
    suspend fun getAll(title: String = ""): List<Habit>

    @Query("SELECT * FROM habit WHERE id = :id LIMIT 1")
    suspend fun findHabitById(id: Int): Habit

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg habit: Habit)

    @Query("DELETE FROM habit")
    suspend fun deleteAll()
}