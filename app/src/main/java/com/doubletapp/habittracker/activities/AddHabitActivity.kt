package com.doubletapp.habittracker.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.databinding.ActivityAddHabitBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.toEditable


class AddHabitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitBinding
    private var chosenHabitType = ""
    private var habit: Habit? = null
    private val colors = listOf(
        Color.parseColor("#F3E575"),
        Color.parseColor("#EAC262"),
        Color.parseColor("#E38E8F"),
        Color.parseColor("#E896E3"),
        Color.parseColor("#BF9FFA"),
        Color.parseColor("#ACB9F2"),
        Color.parseColor("#B0E1FC"),
        Color.parseColor("#B0F7BB"),
        Color.parseColor("#E6FA91"),
        Color.parseColor("#D1BEB1"),
        Color.parseColor("#94C5B8"),
        Color.parseColor("#C5AEE9"),
        Color.parseColor("#B89D18"),
        Color.parseColor("#B84949"),
        Color.parseColor("#88C9E2"),
        Color.parseColor("#87BE8C"),
    )
    private var chosenColorIndex = -1
    private var chosenColor: Int = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = supportActionBar
        toolbar?.title = resources.getString(R.string.toolbar_add_habit_activity)
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowHomeEnabled(true)

        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.habit_type_radio_bad -> chosenHabitType = resources.getString(R.string.habit_type_bad)
                R.id.habit_type_radio_neutral -> chosenHabitType = resources.getString(R.string.habit_type_neutral)
                R.id.habit_type_radio_good -> chosenHabitType = resources.getString(R.string.habit_type_good)
            }
        }

        for (i in colors.indices)
            addColorView(colors[i], i)

        if (intent != null && intent.extras != null
            && intent.extras!!.containsKey(Settings.KEY_EDIT_HABIT)) {
            habit = intent.extras!!.getParcelable(Settings.KEY_EDIT_HABIT)
            if (habit == null)
                throw NullPointerException("Editable habit is null")
            toolbar?.title = resources.getString(R.string.toolbar_edit_habit_activity)
            binding.btnAddHabit.text = resources.getString(R.string.btn_edit_habit)
            setEditableHabit(habit!!)
        }

        val types = resources.getStringArray(R.array.habit_priority_spinner)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, types)
        binding.editHabitPriority.setAdapter(adapter)

        binding.btnAddHabit.setOnClickListener(btnAddHabitOnClickListener)
    }

    private fun setEditableHabit(habit: Habit) {
        binding.editHabitTitle.text = habit.title.toEditable()
        binding.editHabitDescription.text = habit.description.toEditable()
        binding.editHabitPriority.text = habit.priority.toEditable()

        when (habit.type) {
            HabitType.BAD -> {
                binding.habitTypeRadioBad.isChecked = true
                chosenHabitType = resources.getString(R.string.habit_type_bad)
            }
            HabitType.NEUTRAL -> {
                binding.habitTypeRadioNeutral.isChecked = true
                chosenHabitType = resources.getString(R.string.habit_type_neutral)
            }
            HabitType.GOOD -> {
                binding.habitTypeRadioGood.isChecked = true
                chosenHabitType = resources.getString(R.string.habit_type_good)
            }
        }

        binding.editHabitCountComplete.text = habit.countComplete.toString().toEditable()
        binding.editHabitPeriod.text = habit.period.toString().toEditable()

        val indexColor = colors.indexOf(habit.color)
        chosenColorIndex = indexColor
        chosenColor = habit.color
        val colorView = binding.habitColorPickerLayout.findViewById<Button>(indexColor)
        val bg = getDrawable(R.drawable.chosen_color_shape) as GradientDrawable
        bg.color = ColorStateList.valueOf(chosenColor)
        colorView.background = bg
    }

    private val btnAddHabitOnClickListener = View.OnClickListener {
        val title = binding.editHabitTitle.text.toString()
        val description = binding.editHabitDescription.text.toString()
        val priority = binding.editHabitPriority.text.toString()
        val countComplete = binding.editHabitCountComplete.text.toString().toIntOrNull()
        val period = binding.editHabitPeriod.text.toString().toIntOrNull()
        if (title == "" || description == "" || priority == ""
            || countComplete == null || period == null || chosenHabitType == "") {
            Toast.makeText(
                this@AddHabitActivity,
                resources.getString(R.string.toast_fail_add_habit),
                Toast.LENGTH_SHORT
            ).show()
            setErrors(title, description, priority, countComplete, period)
            return@OnClickListener
        }

        val intentKey: String
        if (habit == null) {
            habit = Habit(
                title,
                description,
                priority,
                HabitType.fromString(chosenHabitType),
                countComplete,
                period,
                chosenColor
            )
            intentKey = Settings.KEY_ADD_HABIT_RESULT
        } else {
            habit!!.title = title
            habit!!.description = description
            habit!!.priority = priority
            habit!!.type = HabitType.fromString(chosenHabitType)
            habit!!.countComplete = countComplete
            habit!!.period = period
            habit!!.color = chosenColor
            intentKey = Settings.KEY_EDIT_HABIT_RESULT
        }

        val resultIntent = Intent()
        resultIntent.putExtra(intentKey, habit)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
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
        if (title == "")
            binding.editHabitTitle.error = emptyFieldError
        if (description == "")
            binding.editHabitDescription.error = emptyFieldError
        if (priority == "")
            binding.editHabitPriority.error = emptyFieldError
        if (countComplete == null)
            binding.editHabitCountComplete.error = failNumberError
        if (period == null)
            binding.editHabitPeriod.error = failNumberError
    }

    private fun addColorView(color: Int, index: Int) {
        val dp = resources.displayMetrics.density
        val colorView = Button(this)
        colorView.id = index
        val layoutParams = ViewGroup.MarginLayoutParams(
            (56 * dp).toInt(),
            (56 * dp).toInt()
        )
        layoutParams.leftMargin = (7 * dp).toInt()
        layoutParams.rightMargin = (7 * dp).toInt()
        colorView.layoutParams = layoutParams
        val bg = getDrawable(R.drawable.color_shape) as GradientDrawable
        bg.color = ColorStateList.valueOf(color)
        colorView.background = bg
        colorView.setOnClickListener {
            val currentIndex = it.id
            if (chosenColorIndex != -1) {
                val lastColorView = binding.habitColorPickerLayout.findViewById<Button>(chosenColorIndex)
                val lastBg = getDrawable(R.drawable.color_shape) as GradientDrawable
                lastBg.color = ColorStateList.valueOf(chosenColor)
                lastColorView.background = lastBg
            }
            chosenColorIndex = currentIndex
            chosenColor = colors[currentIndex]
            val chosenBg = getDrawable(R.drawable.chosen_color_shape) as GradientDrawable
            chosenBg.color = ColorStateList.valueOf(colors[index])
            it.background = chosenBg
        }
        binding.habitColorPickerLayout.addView(colorView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}