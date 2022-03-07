package de.bornholdtlee.defaultprojectkotlin.extensions

import android.content.res.Resources

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this == 1

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
