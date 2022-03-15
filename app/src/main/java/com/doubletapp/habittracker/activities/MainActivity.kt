package com.doubletapp.habittracker.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.databinding.ActivityMainBinding
import com.doubletapp.habittracker.models.Habit

class MainActivity : AppCompatActivity(), IHabitClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var habitsAdapter: HabitsAdapter
    private val habits = mutableListOf(
        Habit(
            "Test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Средний",
            com.doubletapp.habittracker.models.HabitType.BAD,
            5,
            10,
            Color.parseColor("#B0E1FC")
        )
    )
    private var lastClickedHabitPosition = -1
    private val resultLauncherEditHabit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK && lastClickedHabitPosition != -1) {
            result.data?.let {
                val editedHabit = it.getParcelableExtra<Habit>(Settings.KEY_EDIT_HABIT_RESULT)
                if (editedHabit != null) {
                    habits[lastClickedHabitPosition] = editedHabit
                    habitsAdapter.notifyItemChanged(lastClickedHabitPosition)
                }
            }
        }
    }
    private val resultLauncherAddHabit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val habit = it.getParcelableExtra<Habit>(Settings.KEY_ADD_HABIT_RESULT)
                if (habit != null) {
                    habits.add(habit)
                    habitsAdapter.notifyItemInserted(habits.lastIndex)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (habits.size != 0) binding.textEmptyHabits.visibility = View.GONE
        habitsAdapter = HabitsAdapter(habits, this)
        binding.habitsList.adapter = habitsAdapter

        binding.fabAddHabit.setOnClickListener {
            val openAddHabitActivity = Intent(this, AddHabitActivity::class.java)
            resultLauncherAddHabit.launch(openAddHabitActivity)
        }
    }

    override fun onHabitClick(position: Int) {
        lastClickedHabitPosition = position
        val habit = habits.getOrNull(position) ?: throw NullPointerException("Clicked habit not found")
        val openEditHabit = Intent(this, AddHabitActivity::class.java)
        openEditHabit.putExtra(Settings.KEY_EDIT_HABIT, habit)
        resultLauncherEditHabit.launch(openEditHabit)
    }
}