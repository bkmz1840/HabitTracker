package com.doubletapp.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doubletapp.habittracker.BuildConfig
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {
    private lateinit var binding: FragmentAppInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppInfoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appVersion = resources.getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        binding.appVersion.text = appVersion
    }
}