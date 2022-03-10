package com.doubletapp.habittracker

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doubletapp.habittracker.models.Habit

class HabitsViewHolder(
    itemView: View,
    private val onHabitClickListener: IHabitClickListener
): RecyclerView.ViewHolder(itemView) {
    private val habitCard: CardView = itemView.findViewById(R.id.habit_card)
    private val habitTitle: TextView = itemView.findViewById(R.id.habit_title)
    private val habitDescription: TextView = itemView.findViewById(R.id.habit_description)
    private val habitPriority: TextView = itemView.findViewById(R.id.habit_priority)
    private val habitType: TextView = itemView.findViewById(R.id.habit_type)
    private val habitCountComplete: TextView = itemView.findViewById(R.id.habit_count_complete)
    private val habitPeriod: TextView = itemView.findViewById(R.id.habit_period)

    fun bind(habit: Habit) {
        val priorityValue = "Приоритет: " + habit.priority
        val typeValue = "Тип: " + habit.type.stringType
        val countCompleteValue = "Количество выполнения: " + habit.countComplete.toString()
        val periodValue = "Количество повторений в день: " + habit.period.toString()

        habitCard.setCardBackgroundColor(habit.color)
        habitTitle.text = habit.title
        habitDescription.text = habit.description
        habitPriority.text = priorityValue
        habitType.text = typeValue
        habitCountComplete.text = countCompleteValue
        habitPeriod.text = periodValue

        itemView.setOnClickListener {
            onHabitClickListener.onHabitClick(adapterPosition)
        }
    }
}