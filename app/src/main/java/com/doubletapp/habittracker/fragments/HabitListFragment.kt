package com.doubletapp.habittracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.doubletapp.habittracker.*
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.databinding.FragmentHabitListBinding
import com.doubletapp.habittracker.util.sortByType
import com.doubletapp.habittracker.viewModels.HabitListViewModel
import com.doubletapp.habittracker.viewModels.HabitListViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HabitListFragment: Fragment(), IHabitListener {
    private lateinit var binding: FragmentHabitListBinding
    private var habitType: HabitType = HabitType.NONE
    private val viewModel: HabitListViewModel by activityViewModels {
        HabitListViewModelFactory(
            (activity?.application as HabitsApplication).appComponent.loadHabitsInteractor()
        )
    }
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
            } else {
                binding.textEmptyHabits.visibility = View.VISIBLE
            }
            habitsAdapter.habits = habits
        }
        viewModel.progressStatus.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressUploadHabits.visibility = View.VISIBLE
                binding.textEmptyHabits.visibility = View.GONE
                binding.habitsList.visibility = View.GONE
            } else {
                binding.habitsList.visibility = View.VISIBLE
                binding.progressUploadHabits.visibility = View.GONE
            }
        }
        binding.habitsList.adapter = habitsAdapter
    }

    override fun onHabitClick(habit: Habit) {
        val bundle = Bundle()
        habit.id?.let { bundle.putInt(Settings.KEY_EDIT_HABIT_ID, it) }
        findNavController().navigate(R.id.nav_add_habit, bundle)
    }

    override fun onHabitComplete(habit: Habit) {
        (activity?.application as HabitsApplication).applicationScope.launch {
            viewModel.submitHabitComplete(habit).collect {
                if (!it) return@collect
                habit.currentComplete += 1
                val msg = if (habit.type == HabitType.BAD) {
                    if (habit.currentComplete <= habit.countComplete)
                        getString(
                            R.string.habit_bad_can_complete,
                            (habit.countComplete - habit.currentComplete).toString()
                        ) else getString(R.string.habit_bad_cannot_complete, habit.title)
                } else {
                    if (habit.currentComplete <= habit.countComplete)
                        getString(
                            R.string.habit_good_can_complete,
                            (habit.countComplete - habit.currentComplete).toString()
                        ) else getString(R.string.habit_good_cannot_complete)
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        }
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