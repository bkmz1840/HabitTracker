package com.doubletapp.habittracker.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.adapters.HabitsPagerAdapter
import com.doubletapp.habittracker.databinding.ActivityMainBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.util.sortByType
import com.google.android.material.tabs.TabLayoutMediator

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
        ),
        Habit(
            "Test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Средний",
            com.doubletapp.habittracker.models.HabitType.GOOD,
            5,
            10,
            Color.parseColor("#B0E1FC")
        ),
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
        setSupportActionBar(binding.activityMainToolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)

        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.navigationDrawerLayout,
            binding.activityMainToolbar,
            R.string.open_drawer_content,
            R.string.close_drawer_content
        )
        binding.navigationDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val sortedHabits = habits.sortByType()
        val tabNames = resources.getStringArray(R.array.tab_names)
        binding.viewPager.adapter = HabitsPagerAdapter(this, sortedHabits)
        TabLayoutMediator(
            binding.habitTabs,
            binding.viewPager
        ) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

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