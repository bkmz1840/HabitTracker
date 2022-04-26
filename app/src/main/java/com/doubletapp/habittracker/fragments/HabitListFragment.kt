package com.doubletapp.habittracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.databinding.FragmentHabitListBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.sortByType
import com.doubletapp.habittracker.viewModels.HabitListViewModel

class HabitListFragment: Fragment(), IHabitClickListener {
    private lateinit var binding: FragmentHabitListBinding
    private var habitType: HabitType = HabitType.NONE
    private val viewModel: HabitListViewModel by activityViewModels()
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habits.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            val habits = it.sortByType().getHabitsByType(habitType)
            if (habits.isNotEmpty()) {
                binding.textEmptyHabits.visibility = View.GONE
            }
            habitsAdapter.habits = habits
        }
        binding.habitsList.adapter = habitsAdapter
    }

    override fun onHabitClick(habit: Habit) {
        val bundle = Bundle()
        habit.id?.let { bundle.putInt(Settings.KEY_EDIT_HABIT_ID, it) }
        findNavController().navigate(R.id.nav_add_habit, bundle)
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