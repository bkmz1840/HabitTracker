package com.doubletapp.habittracker.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.activities.AddHabitActivity
import com.doubletapp.habittracker.adapters.HabitsPagerAdapter
import com.doubletapp.habittracker.databinding.FragmentHomeBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.sortByType
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(), IHabitChangeListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter: HabitsPagerAdapter
    private var habits: HabitList = HabitList(mutableListOf(), mutableListOf())
    private val resultLauncherAddHabit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val habit = it.getParcelableExtra<Habit>(Settings.KEY_ADD_HABIT_RESULT)
                if (habit != null) {
                    habits.addHabit(habit)
                    pagerAdapter.addNewHabit(habit)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        pagerAdapter = HabitsPagerAdapter(
            activity as FragmentActivity,
            habits,
            this,
            activity?.supportFragmentManager
        )
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(
            binding.habitTabs,
            binding.viewPager
        ) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.fabAddHabit.setOnClickListener {
            val openAddHabitActivity = Intent(activity, AddHabitActivity::class.java)
            resultLauncherAddHabit.launch(openAddHabitActivity)
        }
    }

    override fun onHabitChange(habit: Habit, position: Int) {
        habits.updateHabit(habit, position)
    }
}