package com.doubletapp.habittracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.doubletapp.habittracker.databinding.FragmentSearchHabitBinding
import com.doubletapp.habittracker.viewModels.HabitListViewModel

class SearchHabitFragment : Fragment() {
    private lateinit var binding: FragmentSearchHabitBinding
    private val viewModel: HabitListViewModel by activityViewModels()

    private val searchHabitTitleTextWatcher = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val searchTitle = p0.toString()
            viewModel.searchHabits(searchTitle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchHabitBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadHabits()
        binding.editSearchTitle.addTextChangedListener(searchHabitTitleTextWatcher)
        binding.btnSortHabits.setOnClickListener {
            viewModel.sortHabitsByPriority()
        }
    }
}