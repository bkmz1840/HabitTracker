package com.doubletapp.habittracker.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.activities.AddHabitActivity
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.databinding.FragmentHabitListBinding
import com.doubletapp.habittracker.models.Habit
import java.util.ArrayList

interface IHabitChangeListener {
    fun onHabitChange(habit: Habit, position: Int)
}

class HabitListFragment(private val callback: IHabitChangeListener) : Fragment(), IHabitClickListener {
    private lateinit var binding: FragmentHabitListBinding
    private var habits: MutableList<Habit> = mutableListOf()
    private lateinit var habitsAdapter: HabitsAdapter
    private var lastClickedHabitPosition: Int = -1
    private val resultLauncherEditHabit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK && lastClickedHabitPosition != -1) {
            result.data?.let {
                val editedHabit = it.getParcelableExtra<Habit>(Settings.KEY_EDIT_HABIT_RESULT)
                if (editedHabit != null) {
                    habits[lastClickedHabitPosition] = editedHabit
                    habitsAdapter.notifyItemChanged(lastClickedHabitPosition)
                    callback.onHabitChange(editedHabit, lastClickedHabitPosition)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habits = it.getParcelableArrayList<Habit>(ARG_HABITS)?.toMutableList() ?: mutableListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (habits.isNotEmpty()) {
            binding.textEmptyHabits.visibility = View.GONE
        }
        habitsAdapter = HabitsAdapter(habits, this)
        binding.habitsList.adapter = habitsAdapter
    }

    override fun onHabitClick(position: Int) {
        lastClickedHabitPosition = position
        val habit = habits.getOrNull(position) ?: throw NullPointerException("Clicked habit not found")
        val openEditHabit = Intent(context, AddHabitActivity::class.java)
        openEditHabit.putExtra(Settings.KEY_EDIT_HABIT, habit)
        resultLauncherEditHabit.launch(openEditHabit)
    }

    fun addNewHabit(habit: Habit) {
        habits.add(habit)
        habitsAdapter.notifyItemInserted(habits.lastIndex)
    }

    companion object {
        private const val ARG_HABITS = "HABITS"

        @JvmStatic
        fun newInstance(habits: List<Habit>, callback: IHabitChangeListener) =
            HabitListFragment(callback).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_HABITS, ArrayList(habits))
                }
            }
    }
}