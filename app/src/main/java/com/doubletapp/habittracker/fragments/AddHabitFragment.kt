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
import com.doubletapp.habittracker.HabitsApplication
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.databinding.FragmentAddHabitBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitPriority
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.toEditable
import com.doubletapp.habittracker.viewModels.AddHabitViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class AddHabitFragment : Fragment(), IColorPickerListener {
    private lateinit var binding: FragmentAddHabitBinding
    private lateinit var viewModel: AddHabitViewModel
    private lateinit var priorityAdapter: ArrayAdapter<String>
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
                AddHabitViewModel(
                    habitId,
                    (activity?.application as HabitsApplication).appComponent.loadHabitsUseCases()
                ) as T
        }).get(AddHabitViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habit.observe(viewLifecycleOwner) {
            viewModel.habitColor = it.color
            viewModel.habitType = it.type
            viewModel.habitPriority = it.priority
            viewModel.habitPeriod = it.period
            setEditableHabit(it)
            binding.btnAddHabit.text = resources.getString(R.string.btn_edit_habit)
        }
        viewModel.validationErrors.observe(viewLifecycleOwner) {
            if (it.isEmpty()) findNavController().navigateUp()
            else setErrors(it)
        }
        viewModel.progressLoad.observe(viewLifecycleOwner, observerLoadProgress)
        setHabitPriorityAdapter()
        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            viewModel.habitType = when (i) {
                R.id.habit_type_radio_bad -> HabitType.BAD
                R.id.habit_type_radio_good -> HabitType.GOOD
                else -> HabitType.NONE
            }
        }
        binding.editHabitPriority.setOnItemClickListener { _, _, position, _ ->
            when (priorityAdapter.getItem(position)) {
                getString(R.string.habit_priority_low) -> viewModel.habitPriority = HabitPriority.LOW
                getString(R.string.habit_priority_neutral) -> viewModel.habitPriority = HabitPriority.NEUTRAL
                getString(R.string.habit_priority_high) -> viewModel.habitPriority = HabitPriority.HIGH
                else -> viewModel.habitPriority = HabitPriority.NONE
            }
        }
        binding.btnPickHabitPeriod.setOnClickListener(btnPickHabitPeriodListener)
        binding.btnShowColorPicker.setOnClickListener(btnShowColorPicker)
        binding.btnAddHabit.setOnClickListener(btnAddHabitOnClickListener)
    }

    private val btnPickHabitPeriodListener = View.OnClickListener {
        var startDate = MaterialDatePicker.todayInUtcMilliseconds()
        if (viewModel.habitPeriod != -1L) startDate = viewModel.habitPeriod
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.btn_pick_habit_period))
            .setSelection(startDate)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.habitPeriod = it
        }
        activity?.let {
            datePicker.show(
                it.supportFragmentManager,
                "date_picker_dialog"
            )
        }
    }

    private val observerLoadProgress = {  status: Boolean ->
        if (status) {
            binding.btnAddHabit.visibility = View.GONE
            binding.progressLoadHabit.visibility = View.VISIBLE
        } else {
            binding.btnAddHabit.visibility = View.VISIBLE
            binding.progressLoadHabit.visibility = View.GONE
        }
    }

    private fun setHabitPriorityAdapter() {
        activity?.let {
            val types = arrayOf(
                getString(R.string.habit_priority_low),
                getString(R.string.habit_priority_neutral),
                getString(R.string.habit_priority_high)
            )
            priorityAdapter = ArrayAdapter(it, R.layout.spinner_item, types)
            binding.editHabitPriority.setAdapter(priorityAdapter)
        }
    }

    private fun setEditableHabit(habit: Habit) {
        binding.editHabitTitle.text = habit.title.toEditable()
        binding.editHabitDescription.text = habit.description.toEditable()
        binding.editHabitPriority.text = resources.getString(habit.priority.resId).toEditable()
        setHabitPriorityAdapter()
        when (habit.type) {
            HabitType.BAD -> binding.habitTypeRadioBad.isChecked = true
            HabitType.GOOD -> binding.habitTypeRadioGood.isChecked = true
            else -> {}
        }
        binding.editHabitCountComplete.text = habit.countComplete.toString().toEditable()
    }

    private val btnAddHabitOnClickListener = View.OnClickListener {
        val title = binding.editHabitTitle.text.toString()
        val description = binding.editHabitDescription.text.toString()
        val countComplete = binding.editHabitCountComplete.text.toString().toIntOrNull()
        viewModel.validateHabit(title, description, countComplete)
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

    private fun setErrors(errorList: List<String>) {
        val emptyFieldError = resources.getString(R.string.error_field_empty)
        val failNumberError = resources.getString(R.string.error_field_not_number)
        errorList.forEach {
            when (it) {
                Settings.ERROR_FIELD_TITLE -> binding.editHabitTitle.error = emptyFieldError
                Settings.ERROR_FIELD_DESCRIPTION -> binding.editHabitDescription.error = emptyFieldError
                Settings.ERROR_FIELD_PRIORITY -> binding.editHabitPriority.error = emptyFieldError
                Settings.ERROR_FIELD_COUNT_COMPLETE -> binding.editHabitCountComplete.error = failNumberError
            }
        }
        Toast.makeText(
            context,
            resources.getString(R.string.toast_fail_add_habit),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onColorPicked(color: Int) {
        viewModel.habitColor = color
    }
}