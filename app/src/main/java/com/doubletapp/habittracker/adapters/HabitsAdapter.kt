package com.doubletapp.habittracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubletapp.habittracker.HabitsViewHolder
import com.doubletapp.habittracker.IHabitClickListener
import com.doubletapp.habittracker.databinding.HabitBinding
import com.doubletapp.habittracker.models.Habit

class HabitsAdapter(
    private val habits: List<Habit>,
    private val onHabitClickListener: IHabitClickListener
): RecyclerView.Adapter<HabitsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitsViewHolder(
            HabitBinding.inflate(inflater, parent, false),
            onHabitClickListener
        )
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }
}