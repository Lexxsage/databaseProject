package com.lexxsage.nanopost.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Context.resolveColorAttribute(@AttrRes attrId: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attrId, tv, true)
    return tv.data
}
