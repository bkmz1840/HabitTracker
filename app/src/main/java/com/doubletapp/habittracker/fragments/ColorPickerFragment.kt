package com.doubletapp.habittracker.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.databinding.FragmentColorPickerBinding

interface IColorPickerListener {
    fun onColorPicked(color: Int)
}

class ColorPickerFragment : DialogFragment() {
    private lateinit var packageName: String
    private lateinit var binding: FragmentColorPickerBinding
    private lateinit var colors: List<Int>
    private var chosenColor: Int = Color.WHITE
    private var chosenColorIndex: Int = -1
    private var callback: IColorPickerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as IColorPickerListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            packageName = it.getString(ARG_PACKAGE_NAME, "")
            chosenColor = it.getInt(ARG_COLOR, Color.WHITE)
        }
        context?.let { context ->
            val colorNames = resources.getStringArray(R.array.habit_colors)
            val pickedColors = mutableListOf<Int>()
            val packageName = packageName
            colorNames.forEach {
                val colorId = resources.getIdentifier(it, "color", packageName)
                pickedColors.add(
                    ContextCompat.getColor(context, colorId)
                )
            }
            colors = pickedColors.toList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorPickerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in colors.indices) {
            addColorView(colors[i], i)
        }
        if (chosenColor != Color.WHITE) {
            val indexColor = colors.indexOf(chosenColor)
            chosenColorIndex = indexColor
            val colorView = binding.habitColorPickerLayout.findViewById<Button>(indexColor)
            val bg = context?.getDrawable(R.drawable.chosen_color_shape) as GradientDrawable
            bg.color = ColorStateList.valueOf(chosenColor)
            colorView.background = bg
            setChosenColor()
        }
        binding.btnConfirmColor.setOnClickListener {
            callback?.onColorPicked(chosenColor)
            super.dismiss()
        }
    }

    private fun addColorView(color: Int, index: Int) {
        val dp = resources.displayMetrics.density
        val colorView = Button(context)
        colorView.id = index
        val layoutParams = ViewGroup.MarginLayoutParams(
            (56 * dp).toInt(),
            (56 * dp).toInt()
        )
        layoutParams.leftMargin = (7 * dp).toInt()
        layoutParams.rightMargin = (7 * dp).toInt()
        colorView.layoutParams = layoutParams
        val bg = context?.getDrawable(R.drawable.color_shape) as GradientDrawable
        bg.color = ColorStateList.valueOf(color)
        colorView.background = bg
        colorView.setOnClickListener {
            val currentIndex = it.id
            if (chosenColorIndex != -1) {
                val lastColorView = binding.habitColorPickerLayout.findViewById<Button>(chosenColorIndex)
                val lastBg = context?.getDrawable(R.drawable.color_shape) as GradientDrawable
                lastBg.color = ColorStateList.valueOf(chosenColor)
                lastColorView.background = lastBg
            }
            chosenColorIndex = currentIndex
            chosenColor = colors[currentIndex]
            val chosenBg = context?.getDrawable(R.drawable.chosen_color_shape) as GradientDrawable
            chosenBg.color = ColorStateList.valueOf(colors[index])
            it.background = chosenBg
            setChosenColor()
        }
        binding.habitColorPickerLayout.addView(colorView)
    }

    private fun setChosenColor() {
        val bg = context?.getDrawable(R.drawable.color_shape) as GradientDrawable
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

    companion object {
        private const val ARG_PACKAGE_NAME = "PACKAGE_NAME"
        private const val ARG_COLOR = "COLOR"

        @JvmStatic
        fun newInstance(packageName: String, color: Int) =
            ColorPickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PACKAGE_NAME, packageName)
                    putInt(ARG_COLOR, color)
                }
            }
    }
}