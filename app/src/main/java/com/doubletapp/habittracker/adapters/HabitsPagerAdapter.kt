package com.doubletapp.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubletapp.habittracker.fragments.HabitListFragment
import com.doubletapp.habittracker.models.Habit

class HabitsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val habitsList: List<List<Habit>>
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = habitsList.size

    override fun createFragment(position: Int): Fragment =
        HabitListFragment.newInstance(habitsList[position])
}