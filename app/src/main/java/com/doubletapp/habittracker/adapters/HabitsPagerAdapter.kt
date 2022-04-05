package com.doubletapp.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubletapp.habittracker.fragments.HabitListFragment
import com.doubletapp.habittracker.fragments.IHabitChangeListener
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType

class HabitsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val habitsList: HabitList,
    private val habitChangeListener: IHabitChangeListener,
    private val fragmentManager: FragmentManager?,
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = HabitListFragment.newInstance(
        habitsList.getHabitsByType(habitTypeByPosition[position] ?: HabitType.NONE),
        habitChangeListener
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
        private val habitTypeByPosition: Map<Int, HabitType> = mapOf(
            0 to HabitType.GOOD,
            1 to HabitType.BAD,
        )

        private val positionByHabitType: Map<HabitType, Int> = mapOf(
            HabitType.GOOD to 0,
            HabitType.BAD to 1,
        )
    }
}