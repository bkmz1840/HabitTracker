package com.doubletapp.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.adapters.HabitsPagerAdapter
import com.doubletapp.habittracker.databinding.FragmentHomeBinding
import com.doubletapp.habittracker.models.HabitType
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter: HabitsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val habitTypes = listOf(HabitType.GOOD, HabitType.BAD)
        pagerAdapter = HabitsPagerAdapter(
            activity as FragmentActivity,
            habitTypes,
        )
        binding.viewPager.adapter = pagerAdapter
        val tabNames = resources.getStringArray(R.array.tab_names)
        TabLayoutMediator(
            binding.habitTabs,
            binding.viewPager
        ) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.nav_add_habit)
        }
    }
}