package com.doubletapp.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubletapp.habittracker.fragments.HabitListFragment
import com.doubletapp.habittracker.models.HabitType

class HabitsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val habitsTypes: List<HabitType>,
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = habitsTypes.size

    override fun createFragment(position: Int): Fragment =
        HabitListFragment.newInstance(habitsTypes[position])
}