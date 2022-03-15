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
import androidx.core.content.ContextCompat
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.databinding.ActivityAddHabitBinding
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.util.toEditable


class AddHabitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitBinding
    private lateinit var colors: List<Int>
    private var chosenHabitType = HabitType.NONE
    private var habit: Habit? = null
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
            chosenHabitType = when (i) {
                R.id.habit_type_radio_bad -> HabitType.BAD
                R.id.habit_type_radio_neutral -> HabitType.NEUTRAL
                R.id.habit_type_radio_good -> HabitType.GOOD
                else -> HabitType.NONE
            }
        }

        val colorNames = resources.getStringArray(R.array.habit_colors)
        val pickedColors = mutableListOf<Int>()
        val packageName = packageName
        colorNames.forEach {
            val colorId = resources.getIdentifier(it, "color", packageName)
            pickedColors.add(
                ContextCompat.getColor(this, colorId)
            )
        }
        colors = pickedColors.toList()
        for (i in colors.indices) {
            addColorView(colors[i], i)
        }

        habit = intent.getParcelableExtra(Settings.KEY_EDIT_HABIT)
        habit?.let {
            setEditableHabit(it)
            toolbar?.title = resources.getString(R.string.toolbar_edit_habit_activity)
            binding.btnAddHabit.text = resources.getString(R.string.btn_edit_habit)
        }
        setChosenColor()

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
            HabitType.BAD -> binding.habitTypeRadioBad.isChecked = true
            HabitType.NEUTRAL -> binding.habitTypeRadioNeutral.isChecked = true
            HabitType.GOOD -> binding.habitTypeRadioGood.isChecked = true
            else -> throw IllegalArgumentException("Unexpected habit type")
        }
        chosenHabitType = habit.type

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
        if (title.isEmpty() || description.isEmpty() || priority.isEmpty()
            || countComplete == null || period == null || chosenHabitType == HabitType.NONE) {
            Toast.makeText(
                this@AddHabitActivity,
                resources.getString(R.string.toast_fail_add_habit),
                Toast.LENGTH_SHORT
            ).show()
            setErrors(title, description, priority, countComplete, period)
            return@OnClickListener
        }

        var intentKey = ""
        habit?.apply {
            this.title = title
            this.description = description
            this.priority = priority
            this.type = chosenHabitType
            this.countComplete = countComplete
            this.period = period
            this.color = chosenColor
            intentKey = Settings.KEY_EDIT_HABIT_RESULT
        } ?: run {
            habit = Habit(
                title,
                description,
                priority,
                chosenHabitType,
                countComplete,
                period,
                chosenColor
            )
            intentKey = Settings.KEY_ADD_HABIT_RESULT
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
            setChosenColor()
        }
        binding.habitColorPickerLayout.addView(colorView)
    }

    private fun setChosenColor() {
        val bg = getDrawable(R.drawable.color_shape) as GradientDrawable
        bg.color = ColorStateList.valueOf(chosenColor)
        binding.habitChosenColor.background = bg

        val chosenRgb = arrayOf(
            Color.red(chosenColor),
            Color.green(chosenColor),
            Color.blue(chosenColor)
        )
        val chosenHsv = FloatArray(3)
        Color.colorToHSV(chosenColor, chosenHsv)
        val chosenRgbText = "R - ${chosenRgb[0]}, G - ${chosenRgb[1]}, B - ${chosenRgb[2]}"
        val chosenHsvText = "H - ${chosenHsv[0]}, S - ${chosenHsv[1]}, V - ${chosenHsv[2]}"
        binding.habitChosenColorRgb.text = chosenRgbText
        binding.habitChosenColorRgb.setTextColor(chosenColor)
        binding.habitChosenColorHsv.text = chosenHsvText
        binding.habitChosenColorHsv.setTextColor(chosenColor)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}