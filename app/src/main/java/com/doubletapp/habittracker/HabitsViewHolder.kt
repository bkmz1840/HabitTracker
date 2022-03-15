package com.doubletapp.habittracker

import androidx.recyclerview.widget.RecyclerView
import com.doubletapp.habittracker.databinding.HabitBinding
import com.doubletapp.habittracker.models.Habit

interface IHabitClickListener {
    fun onHabitClick(position: Int)
}

class HabitsViewHolder(
    private val binding: HabitBinding,
    private val onHabitClickListener: IHabitClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        val priorityValue = "Приоритет: " + habit.priority
        val typeValue = "Тип: " + itemView.resources.getString(habit.type.resId)
        val countCompleteValue = "Количество выполнения: " + habit.countComplete.toString()
        val periodValue = "Количество повторений в день: " + habit.period.toString()

        binding.habitCard.setCardBackgroundColor(habit.color)
        binding.habitTitle.text = habit.title
        binding.habitDescription.text = habit.description
        binding.habitPriority.text = priorityValue
        binding.habitType.text = typeValue
        binding.habitCountComplete.text = countCompleteValue
        binding.habitPeriod.text = periodValue

        itemView.setOnClickListener {
            onHabitClickListener.onHabitClick(adapterPosition)
        }
    }
}