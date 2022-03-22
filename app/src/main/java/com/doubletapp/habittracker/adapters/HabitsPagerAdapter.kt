package com.doubletapp.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubletapp.habittracker.fragments.HabitListFragment
import com.doubletapp.habittracker.fragments.IHabitChangeListener
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType

class HabitsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val habitsList: List<List<Habit>>,
    private val habitChangeListener: IHabitChangeListener,
    private val fragmentManager: FragmentManager?,
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = habitsList.size

    override fun createFragment(position: Int): Fragment = HabitListFragment.newInstance(
        habitsList[position], habitChangeListener
    )

    fun addNewHabit(habit: Habit) {
        val fragmentPosition = positionByHabitType[habit.type] ?: throw IllegalArgumentException(
            "Unexpected habit type ${habit.type}"
        )
        val fragment = fragmentManager?.findFragmentByTag(
            "f" + getItemId(fragmentPosition)
        ) ?: throw NullPointerException("Fragment manager not found")
        (fragment as HabitListFragment).addNewHabit(habit)
    }

    companion object {
        private val positionByHabitType: Map<HabitType, Int> = mapOf(
            HabitType.GOOD to 0,
            HabitType.BAD to 1,
        )
    }
}