package ru.alexoheah.newsreader.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.format(): String {
    val date = Date(this)
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(date)
}