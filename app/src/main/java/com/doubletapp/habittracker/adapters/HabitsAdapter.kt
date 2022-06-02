package com.doubletapp.habittracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.HabitsViewHolder
import com.doubletapp.habittracker.IHabitListener
import com.doubletapp.habittracker.databinding.HabitBinding

class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {

    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean = areItemsTheSame(oldItem, newItem) ||
        oldItem.description == newItem.description
}

class HabitsAdapter(
    _habits: List<Habit>,
    private val onHabitListener: IHabitListener,
): ListAdapter<Habit, HabitsViewHolder>(HabitDiffCallback()) {
    var habits: List<Habit> = _habits
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitsViewHolder(
            HabitBinding.inflate(inflater, parent, false),
            onHabitListener
        )
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }
}