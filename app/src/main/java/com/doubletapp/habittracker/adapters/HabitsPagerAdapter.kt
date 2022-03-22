package com.doubletapp.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubletapp.habittracker.fragments.HabitListFragment
import com.doubletapp.habittracker.fragments.IHabitChangeListener
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType

class HabitsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val habitsList: List<List<Habit>>,
    private val habitChangeListener: IHabitChangeListener
): FragmentStateAdapter(fragmentActivity) {
    private val pages = mutableListOf<HabitListFragment>()

    override fun getItemCount(): Int = habitsList.size

    override fun createFragment(position: Int): Fragment {
        val page = HabitListFragment.newInstance(habitsList[position], habitChangeListener)
        pages.add(position, page)
        return page
    }

    fun addNewHabit(habit: Habit) {
        val fragmentPosition = positionByHabitType[habit.type] ?: throw IllegalArgumentException(
            "Unexpected habit type ${habit.type}"
        )
        val fragment = pages[fragmentPosition]
        fragment.addNewHabit(habit)
    }

    companion object {
        private val positionByHabitType: Map<HabitType, Int> = mapOf(
            HabitType.GOOD to 0,
            HabitType.BAD to 1,
        )
    }
}