package com.doubletapp.habittracker.util

import android.text.Editable

fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)