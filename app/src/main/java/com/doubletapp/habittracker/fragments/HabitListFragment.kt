package com.doubletapp.habittracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.adapters.HabitsAdapter
import com.doubletapp.habittracker.databinding.FragmentHabitListBinding
import com.doubletapp.habittracker.models.Habit
import java.util.ArrayList

class HabitListFragment : Fragment(), IHabitClickListener {
    private lateinit var binding: FragmentHabitListBinding
    private var habits: List<Habit> = listOf()
    private lateinit var habitsAdapter: HabitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habits = it.getParcelableArrayList<Habit>(ARG_HABITS)?.toList() ?: listOf()
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
        TODO("Not yet implemented")
    }

    companion object {
        private const val ARG_HABITS = "HABITS"

        @JvmStatic
        fun newInstance(habits: List<Habit>) =
            HabitListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_HABITS, ArrayList(habits))
                }
            }
    }
}