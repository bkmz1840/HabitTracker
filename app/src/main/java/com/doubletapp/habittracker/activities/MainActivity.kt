package com.doubletapp.habittracker.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.adapters.HabitsPagerAdapter
import com.doubletapp.habittracker.databinding.ActivityMainBinding
import com.doubletapp.habittracker.fragments.IHabitChangeListener
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.sortByType
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), IHabitChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: HabitsPagerAdapter
    private var habits: Map<HabitType, MutableList<Habit>> = mapOf()
    private val resultLauncherAddHabit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val habit = it.getParcelableExtra<Habit>(Settings.KEY_ADD_HABIT_RESULT)
                if (habit != null) {
                    habits[habit.type]?.add(habit)
                    pagerAdapter.addNewHabit(habit)
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

        val habitsList = listOf(
            Habit(
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний", HabitType.BAD,  5, 10,
                Color.parseColor("#B0E1FC")
            ),
            Habit(
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний", HabitType.GOOD, 5, 10,
                Color.parseColor("#B0E1FC")
            ),
            Habit(
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний",  HabitType.BAD, 5,10,
                Color.parseColor("#B0E1FC")
            )
        )
        habits = habitsList.sortByType()
        val tabNames = resources.getStringArray(R.array.tab_names)
        val pages = listOf(
            habits[HabitType.GOOD]?.toList() ?: listOf(),
            habits[HabitType.BAD]?.toList() ?: listOf(),
        )
        pagerAdapter = HabitsPagerAdapter(this, pages)
        binding.viewPager.adapter = pagerAdapter
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

    override fun onHabitChange(habit: Habit, position: Int) {
        habits[habit.type]?.let {
            it[position] = habit
        }
    }
}