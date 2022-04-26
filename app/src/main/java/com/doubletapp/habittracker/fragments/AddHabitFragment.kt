package com.doubletapp.habittracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.databinding.FragmentAddHabitBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.toEditable
import com.doubletapp.habittracker.viewModels.AddHabitViewModel

class AddHabitFragment : Fragment(), IColorPickerListener {
    private lateinit var binding: FragmentAddHabitBinding
    private lateinit var viewModel: AddHabitViewModel
    private var habitId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitId = it.getInt(Settings.KEY_EDIT_HABIT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHabitBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                AddHabitViewModel(habitId) as T
        }).get(AddHabitViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habit.observe(viewLifecycleOwner) {
            viewModel.habitColor = it.color
            viewModel.habitType = it.type
            setEditableHabit(it)
            binding.btnAddHabit.text = resources.getString(R.string.btn_edit_habit)
        }
        setHabitPriorityAdapter()
        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            viewModel.habitType = when (i) {
                R.id.habit_type_radio_bad -> HabitType.BAD
                R.id.habit_type_radio_good -> HabitType.GOOD
                else -> HabitType.NONE
            }
        }
        binding.btnShowColorPicker.setOnClickListener(btnShowColorPicker)
        binding.btnAddHabit.setOnClickListener(btnAddHabitOnClickListener)
    }

    private fun setHabitPriorityAdapter() {
        activity?.let {
            val types = resources.getStringArray(R.array.habit_priority_spinner)
            val adapter = ArrayAdapter(it, R.layout.spinner_item, types)
            binding.editHabitPriority.setAdapter(adapter)
        }
    }

    private fun setEditableHabit(habit: Habit) {
        binding.editHabitTitle.text = habit.title.toEditable()
        binding.editHabitDescription.text = habit.description.toEditable()
        binding.editHabitPriority.text = habit.priority.toEditable()
        setHabitPriorityAdapter()
        when (habit.type) {
            HabitType.BAD -> binding.habitTypeRadioBad.isChecked = true
            HabitType.GOOD -> binding.habitTypeRadioGood.isChecked = true
            else -> throw IllegalArgumentException("Unexpected habit type")
        }

        binding.editHabitCountComplete.text = habit.countComplete.toString().toEditable()
        binding.editHabitPeriod.text = habit.period.toString().toEditable()
    }

    private val btnAddHabitOnClickListener = View.OnClickListener {
        val title = binding.editHabitTitle.text.toString()
        val description = binding.editHabitDescription.text.toString()
        val priority = binding.editHabitPriority.text.toString()
        val countComplete = binding.editHabitCountComplete.text.toString().toIntOrNull()
        val period = binding.editHabitPeriod.text.toString().toIntOrNull()
        if (title.isEmpty() || description.isEmpty() || priority.isEmpty()
            || countComplete == null || period == null || viewModel.habitType == HabitType.NONE) {
            Toast.makeText(
                context,
                resources.getString(R.string.toast_fail_add_habit),
                Toast.LENGTH_SHORT
            ).show()
            setErrors(title, description, priority, countComplete, period)
            return@OnClickListener
        }
        viewModel.uploadHabit(title, description, priority, countComplete, period)
        findNavController().navigateUp()
    }

    private val btnShowColorPicker = View.OnClickListener {
        activity?.let {
            ColorPickerFragment.newInstance(
                it.packageName,
                viewModel.habitColor,
                this
            ).show(
                it.supportFragmentManager,
                "color_picker_dialog"
            )
        }
    }

    private fun setErrors(
        title: String,
        description: String,
        priority: String,
        countComplete: Int?,
        period: Int?
    ) {
        val emptyFieldError = resources.getString(R.string.error_field_empty)
        val failNumberError = resources.getString(R.string.error_field_not_number)
        if (title.isEmpty())
            binding.editHabitTitle.error = emptyFieldError
        if (description.isEmpty())
            binding.editHabitDescription.error = emptyFieldError
        if (priority.isEmpty())
            binding.editHabitPriority.error = emptyFieldError
        if (countComplete == null)
            binding.editHabitCountComplete.error = failNumberError
        if (period == null)
            binding.editHabitPeriod.error = failNumberError
    }

    override fun onColorPicked(color: Int) {
        viewModel.habitColor = color
    }
}