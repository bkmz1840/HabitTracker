package com.doubletapp.habittracker

import androidx.recyclerview.widget.RecyclerView
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.databinding.HabitBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IHabitClickListener {
    fun onHabitClick(habit: Habit)
}

interface IHabitCompleteListener {
    fun onHabitComplete(habit: Habit)
}

class HabitsViewHolder(
    private val binding: HabitBinding,
    private val onHabitClickListener: IHabitClickListener,
    private val onHabitCompleteListener: IHabitCompleteListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        val r = itemView.resources
        val typeStr = r.getString(habit.type.resId)
        val priorityStr = r.getString(habit.priority.resId)

        binding.habitCard.setCardBackgroundColor(habit.color)
        binding.habitTitle.text = habit.title
        binding.habitDescription.text = habit.description
        binding.habitPriority.text = r.getString(R.string.habit_card_priority, priorityStr)
        binding.habitType.text = r.getString(R.string.habit_card_type, typeStr)
        binding.habitCountComplete.text = r.getString(
            R.string.habit_card_count_complete,
            habit.countComplete.toString()
        )
        binding.habitPeriod.text = r.getString(
            R.string.habit_card_priority,
            habit.period.toString()
        )

        itemView.setOnClickListener {
            onHabitClickListener.onHabitClick(habit)
        }

        binding.btnHabitSubmitComplete.setOnClickListener {
            onHabitCompleteListener.onHabitComplete(habit)
        }
    }
}