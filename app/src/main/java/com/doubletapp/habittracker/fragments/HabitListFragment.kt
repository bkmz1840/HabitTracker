package com.doubletapp.habittracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.activities.AddHabitActivity
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.databinding.FragmentHabitListBinding
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.viewModels.HabitListViewModel

class HabitListFragment: Fragment(), IHabitClickListener {
    private lateinit var binding: FragmentHabitListBinding
    private var habitType: HabitType = HabitType.NONE
    private lateinit var viewModel: HabitListViewModel
    private var habitsAdapter = HabitsAdapter(listOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitType = it.getParcelable(ARG_HABIT_TYPE) ?: HabitType.NONE
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
        viewModel = ViewModelProvider(this).get(HabitListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habits.observe(viewLifecycleOwner) {
            val habits = it.getHabitsByType(habitType)
            if (habits.isNotEmpty()) {
                binding.textEmptyHabits.visibility = View.GONE
            }
            habitsAdapter.habits = habits
        }
        binding.habitsList.adapter = habitsAdapter
    }

    override fun onHabitClick(position: Int) {
        val habit = viewModel.getHabit(habitType, position)
        val openEditHabit = Intent(context, AddHabitActivity::class.java)
        openEditHabit.putExtra(Settings.KEY_EDIT_HABIT, habit)
        startActivity(openEditHabit)
    }

    companion object {
        private const val ARG_HABIT_TYPE = "HABIT TYPE"

        @JvmStatic
        fun newInstance(habitType: HabitType) =
            HabitListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HABIT_TYPE, habitType)
                }
            }
    }
}